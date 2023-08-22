package com.project.tedi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

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

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RecommendationService {
	private final UserSearchRepository userSearchRepo;
	private final UserRepository userRepo;
	private final AccomodationRepository accomodationRepo;
	private final RatingRepository ratingRepo;
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
    
    public RecommendationService(UserSearchRepository userSearchRepo,UserRepository userRepo,
    		AccomodationRepository accomodationRepo, RatingRepository ratingRepo) {
    	this.userSearchRepo=userSearchRepo;
    	this.accomodationRepo=accomodationRepo;
    	this.userRepo=userRepo;
    	this.ratingRepo=ratingRepo;
    	this.accomodationCount=0;
    	this.usersCount=0;
    	this.ratingCount=0;
    }
    
    public List<Accomodation> recommend() {
    	//experimental - will delete after and make async
    	factorize();
    	//reasons to return null 
    	User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (this.recommendationMatrix==null ||
    			loggedIn == null || 
    			!usersMap.containsKey(loggedIn.getId()) ||
    			usersMap.get(loggedIn.getId()) >= userMatrix.length
    		) return null;
    	
    	int[] indices = new int[this.accomodationCount];
        for (int i = 0; i < this.accomodationCount; i++) {
            indices[i] = i;
        }
                
        int userInd=this.usersMap.get(loggedIn.getId());
        final double[] row = this.recommendationMatrix[userInd];
        //create priority queue that sorts indices based on the values of the recommendation matrix
        PriorityQueue<Integer> pq = new PriorityQueue<>(maxRecommendations, (a, b) -> Double.compare(row[a], row[b]));
        
        for (int i = 0; i < row.length; i++) {
            pq.offer(i);
            if (pq.size() > maxRecommendations) {
                pq.poll();
            }
        }
        int recommendations = maxRecommendations <= row.length ? maxRecommendations : row.length;
        int[] accomodationIndices = new int[recommendations];
        int index = recommendations - 1;
        //put the smallest in the last index and fill the matrix in reverse order
        while (!pq.isEmpty()) {
            accomodationIndices[index--] = pq.poll();
        }
        
        List<Accomodation> recommendedAccs = new ArrayList<>();
        for (int i : accomodationIndices) {
        	for (Map.Entry<Long, Integer> entry : this.accomodationsMap.entrySet()) {
                if (entry.getValue() == i) {
                	recommendedAccs.add(accomodationRepo.findById(entry.getKey()).orElseThrow(
                			() -> new ResourceNotFoundException("accomodation not found")));
                	break;
                }
            }
        }
        
        return recommendedAccs;
    	
    }
    
    public void factorize() {
    	initializeMatrixes();
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
            if ( rmse+patience < se ) {            	
            	se = rmse; //update squared error
            }
            else {
            	System.out.println("got it");
            	break; // if rmse == se (error not getting better reached local or global minimum)
            }
        }
    	computeRecommendationMatrix();
    }
    
	private void initializeMatrixes() {
    	List<User> users=this.userRepo.findAll();
    	List<Accomodation> accomodations=this.accomodationRepo.findAll();
    	List<Rating> ratings = ratingRepo.findAll();
    	
    	//only remake table if there has been a change of data
    	if (!(users.size()!=this.usersCount || 
    			accomodations.size() != this.accomodationCount || 
    			ratings.size()!=this.ratingCount)
    		) return;
    	
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
		 printMatrix(this.recommendationMatrix);
	}

	 private static void printMatrix(double[][] matrix) {
	        for (int i = 0; i < matrix.length; i++) {
	            for (int j = 0; j < matrix[i].length; j++) {
	                System.out.print(matrix[i][j] + " ");
	            }
	            System.out.println();
	        }
	    }
	 
}
