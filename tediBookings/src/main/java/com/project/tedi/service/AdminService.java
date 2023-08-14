package com.project.tedi.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Booking;
import com.project.tedi.model.Rating;
import com.project.tedi.model.Role;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.BookingsRepository;
import com.project.tedi.repository.RatingRepository;
import com.project.tedi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepo;
	private final AccomodationRepository accRepo;
	private final BookingsRepository bookingRepo;
	private final RatingRepository ratingRepo;
	
	@Transactional
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	public List<User> getAllHosts(){
		return userRepo.findHosts();
	}
	
	public List<User> getAllRenters(){
		return userRepo.findRenters();
	}
	
	@Transactional
	public List<Accomodation> getAllAccomodations(){
		return accRepo.findAll();
	}
	
	@Transactional
	public List<Booking> getAllBookings(){
		return bookingRepo.findAll();
	}
	
	@Transactional
	public List<Rating> getAllRatingsByGuest(Long id){
		return ratingRepo.findByGuestId(id);
	}
	
	@Transactional
	public List<Rating> getAllRatingsByHost(Long id){
		return ratingRepo.findByHostId(id);
	}
	
	@Transactional
	public User approveUser(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format("user with id %ld not found", id)));
		if (user.getApproved()) return user;
		if (user.getRole() == Role.RENTER) {
			user.setRole(Role.HOST_AND_RENTER);
			user.setApproved(true);
			userRepo.save(user);
			return user;
		}
		if (user.getRole() == Role.PENDING) {
			user.setRole(Role.HOST);
			user.setApproved(true);
			userRepo.save(user);
			return user;
		}
		return user;
	}
}
