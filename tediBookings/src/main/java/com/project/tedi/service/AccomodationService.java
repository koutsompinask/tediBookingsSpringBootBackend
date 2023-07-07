package com.project.tedi.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccomodationService {

	private final AccomodationRepository accRepo;
	
	public Accomodation addAcc(Accomodation acc) {
		acc.setOwner(getUserFromContext());
		accRepo.save(acc);
		return acc;
	}
	
	private User getUserFromContext() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
}
