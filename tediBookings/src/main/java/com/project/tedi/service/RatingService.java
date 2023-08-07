package com.project.tedi.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.RatingRepository;

import jakarta.transaction.Transactional;

import com.project.tedi.exception.NotLoggedInException;
import com.project.tedi.exception.ResourceNotFoundException;
import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Rating;
import com.project.tedi.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {

	private final RatingRepository ratingRepo;
	private final AccomodationRepository accomodationRepo;
	
	public List<Rating> getAccomodationRatings(Long accId){
		return ratingRepo.findByAccomodationId(accId);
	};
	
	public List<Rating> getHostRatings(Long accId){
		return ratingRepo.findByHostId(accId);
	};
	
	public void rateAccomodation(Long accId,Rating rating) {
		if (rating.getStars()<0 || rating.getStars()>5) throw new TediBookingsException("rating stars must be 0-5");
		Accomodation acc=accomodationRepo.findById(accId).orElseThrow(
				() -> new ResourceNotFoundException(String.format("no accomodation found with id %d",accId)));
		User loggedIn = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loggedIn==null) throw new NotLoggedInException("you are not logged in");
		else {
			rating.setGuest(loggedIn);
			rating.setAccomodation(acc);
			ratingRepo.save(rating);
		}
	}
	
}
