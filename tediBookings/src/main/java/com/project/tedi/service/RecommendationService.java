package com.project.tedi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.exception.ResourceNotFoundException;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Rating;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.RatingRepository;
import com.project.tedi.repository.UserRepository;
import com.project.tedi.repository.UserSearchRepository;
import com.project.tedi.repository.UserViewAccomodationRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
@EnableAsync
@EnableScheduling
public class RecommendationService {
	private final UserSearchRepository userSearchRepo;
	private final UserRepository userRepo;
	private final AccomodationRepository accomodationRepo;
	private final RatingRepository ratingRepo;
	private final UserViewAccomodationRepository userViewAccRepo;
	private double[][] userMatrix;
	private double[][] accomodationMatrix;
	private double[][] knownRatings;
	private double[][] recommendationMatrix;
    private Map<Long, Integer> accomodationsMap;
    private Map<Long, Integer> usersMap;
    private int accomodationCount,usersCount,ratingCount;

    private static final double learningRate = 0.001;
    private static final double lambda = 0.1;
    private static final int latentFeatures = 3;
    private static final double patience = 0.001;
    private static final int maxIters = 300;
    private static final Random random = new Random();
    private static final int maxRecommendations=5;
    private final Semaphore factorizeSemaphore = new Semaphore(1); // Initialize with 1 permit
    
    public RecommendationService(UserSearchRepository userSearchRepo,UserRepository userRepo,
    		AccomodationRepository accomodationRepo, RatingRepository ratingRepo, UserViewAccomodationRepository userViewAccRepo) {
    	this.userSearchRepo=userSearchRepo;
    	this.accomodationRepo=accomodationRepo;
    	this.userRepo=userRepo;
    	this.ratingRepo=ratingRepo;
    	this.userViewAccRepo = userViewAccRepo;
    	this.accomodationCount=0;
    	this.usersCount=0;
    	this.ratingCount=0;
    }
    
    private class IndexedValue implements Comparable<IndexedValue> {
        int index;
        double value;

        public IndexedValue(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(IndexedValue other) {
            return Double.compare(other.value, this.value);
        }
    }
    
    @Async
    @Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour in milliseconds
    public void scheduledFactorize() throws InterruptedException {
        try{
        	factorizeSemaphore.acquire();
        	factorize();
        } catch (InterruptedException e) {
			throw e;
		} finally {
        	factorizeSemaphore.release();
        	System.out.println("refactorised");
        }
    }
    
    public List<Accomodation> recommend() throws InterruptedException {
    	try{
    		//use semaphore to avoid calling recommend while factorizing (wait for it)
        	factorizeSemaphore.acquire();
        	
        	//uncomment to call factorize every time before recommend
        	//factorize();
        	
        	//reasons to return null 
        	User loggedIn = null; 
        	Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	if (userObj instanceof User) {
        		loggedIn = (User) userObj;
        	}
        	if (this.recommendationMatrix==null ||
        			loggedIn == null ||
        			!usersMap.containsKey(loggedIn.getId()) ||
        			usersMap.get(loggedIn.getId()) >= userMatrix.length
        			) 
        		return null;
        	
        	final double[] row = this.recommendationMatrix[this.usersMap.get(loggedIn.getId())];
        	
        	List<IndexedValue> indexedValues = IntStream.range(0, row.length)
        			.mapToObj(i -> new IndexedValue(i, row[i]))
        			.sorted()
        			.limit(maxRecommendations) // Limit to the top 5 largest values
        			.collect(Collectors.toList());
        	
        	List<Integer> largestIndices = indexedValues.stream()
        			.map(indexedValue -> indexedValue.index)
        			.collect(Collectors.toList());
        	
        	List<Accomodation> recommendedAccs = new ArrayList<>();
        	for (int i : largestIndices) {
        		for (Map.Entry<Long, Integer> entry : this.accomodationsMap.entrySet()) {
        			if (entry.getValue() == i) {
        				recommendedAccs.add(accomodationRepo.findById(entry.getKey()).orElseThrow(
        						() -> new ResourceNotFoundException("accomodation not found")));
        				break;
        			}
        		}
        	}
        	return recommendedAccs;
    	} catch (InterruptedException e) {
    		throw e;
    	} finally {
    		factorizeSemaphore.release();
    	}
    	
    }
    
    public void factorize() {
    	if(!initializeMatrixes()) return;//no change
    	double se=Double.MAX_VALUE;
    	for (int iter = 0; iter < maxIters; iter++) {
    		double rmse=0;
            for (int i = 0; i < this.usersCount; i++) {
                for (int j = 0; j < this.accomodationCount; j++) {
                	//if its known
                    if (this.knownRatings[i][j] > 0) {
                        double prediction = 0;
                        //compute prediction
                        for (int k = 0; k < latentFeatures; k++) {
                            prediction += userMatrix[i][k] * accomodationMatrix[k][j];
                        }
                        double error = knownRatings[i][j] - prediction;
                        rmse+=error*error;
                        for (int k = 0; k < latentFeatures; k++) {
                            userMatrix[i][k] += learningRate * (2 * error * accomodationMatrix[k][j] - lambda * userMatrix[i][k]);
                            accomodationMatrix[k][j] += learningRate * (2 * error * userMatrix[i][k] - lambda * accomodationMatrix[k][j]);
                        }
                    }
                }
            }
            if ( rmse+patience < se ) 
            	se = rmse; //update squared error
            else 
            	break; // if rmse == se (error not getting better reached local or global minimum)
        }
    	computeRecommendationMatrix();
    }
    
	private boolean initializeMatrixes() {
    	List<User> users=this.userRepo.findRenters();
    	List<Accomodation> accomodations=this.accomodationRepo.findAvailableAccomodations();
    	List<Rating> ratings = ratingRepo.findAll();
    	//only remake table if there are enough data
    	if (users.isEmpty() || accomodations.isEmpty()) return false;
    	
    	//else ... (update counts and continue)
    	this.usersCount=users.size();
    	this.accomodationCount=accomodations.size();
    	this.ratingCount=ratings.size();
    	
    	//init V table and the map between user id and index in V
    	this.userMatrix= new double[users.size()][latentFeatures];
    	this.usersMap= new HashMap<>();
		for (int i=0 ; i < users.size() ; i++) {
			for (int j=0 ; j < latentFeatures ; j++ ) {
				this.userMatrix[i][j]=random.nextDouble();
			}
			this.usersMap.put(users.get(i).getId(), i);
    	}
		
		//init F table and the map between acc id and index in F
    	this.accomodationMatrix= new double[latentFeatures][accomodations.size()];
    	this.accomodationsMap = new HashMap<>();
		for (int i=0 ; i < accomodations.size() ; i++) {
			for (int j=0 ; j < latentFeatures ; j++ ) {
				this.accomodationMatrix[j][i]=random.nextDouble();//matrix is inverted
			}
			this.accomodationsMap.put(accomodations.get(i).getId(), i);
    	}
		
		this.knownRatings= new double[users.size()][accomodations.size()];
		for (Rating r : ratings) {
			int accIndex = accomodationsMap.get(r.getAccomodation().getId());
			int userIndex = usersMap.get(r.getGuest().getId());
			this.knownRatings[userIndex][accIndex]=(double) (r.getStars()/5.0);//normalize
		}
		//add the viewed and search accomodations ( search is less significant than view )
		for (User u: users) {//for each renter
			for (Accomodation a: accomodations) {
				int userIndex = usersMap.get(u.getId());
				int accIndex = accomodationsMap.get(a.getId());
				if (this.knownRatings[userIndex][accIndex]!=0) continue;
				int userSearchedTimes = userSearchRepo.countByUserAndAccomodationId(u.getId(),a.getId());
				int userViewedTimes = userViewAccRepo.countByUserAndAccomodationId(u.getId(),a.getId());
				if(userSearchedTimes+userViewedTimes == 0) continue;
				double score = 0.5 + userSearchedTimes*0.1 + userViewedTimes*0.2;//favor accommodations that user has searched or viewed
				score = score <=1.0  ? score : 1.0;
				this.knownRatings[userIndex][accIndex] = score;
			}
		}
		//uncomment to see matrix
		//printMatrix(knownRatings);
		return true;
		
    }
	
	 private void computeRecommendationMatrix() {
		 int numRows = this.userMatrix.length;
		 int numCols = this.accomodationMatrix[0].length;

		 this.recommendationMatrix = new double[numRows][numCols];

		 for (int i = 0; i < numRows; i++) {
			 for (int j = 0; j < numCols; j++) {
				 double product = 0;
				 for (int k = 0; k < latentFeatures; k++) {
					 product += userMatrix[i][k] * accomodationMatrix[k][j];
				 }
				 this.recommendationMatrix[i][j] = product;
			 }
		 }
	}
	 
	 public static void printMatrix(double[][] matrix) {
	        int numRows = matrix.length;
	        int numCols = matrix[0].length;

	        for (int i = 0; i < numRows; i++) {
	            for (int j = 0; j < numCols; j++) {
	                System.out.printf("%10.4f ", matrix[i][j]);
	            }
	            System.out.println();
	        }
	    }
}
