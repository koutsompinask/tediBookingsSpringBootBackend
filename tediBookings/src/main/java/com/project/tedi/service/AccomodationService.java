package com.project.tedi.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.tedi.dto.SearchRequest;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccomodationService {

	private final AccomodationRepository accRepo;
	private final UserRepository userRepo;
	
	@Transactional
	public Accomodation addAcc(Accomodation acc) {
		acc.setOwner((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		accRepo.save(acc);
		return acc;
	}
	
	@Transactional
	public List<Accomodation> getAll(){
		return accRepo.findAll();
	}

	public List<Accomodation> getFiltered(SearchRequest searchReq) {
		int people = searchReq.getNumPerson();
		String loc = searchReq.getLocation();
		Date from = searchReq.getFrom();
		Date to = searchReq.getTo();
		if ((loc==null || loc.isEmpty()) && from==null && to==null) {
			return accRepo.filteredAccomodationsByPeople(people);
		}
		else if (loc==null || loc.isEmpty()){
			return accRepo.filteredAccomodationsByPeopleAndAvailability(people, from, to);
		}
		else {
			return accRepo.filteredAccomodationsByPeopleAndLocation(people, loc);
		}
	}
	
	public List<Accomodation> getByOwner(Long id){
		return accRepo.findByOwnerId(id);
	}
	
	
}
