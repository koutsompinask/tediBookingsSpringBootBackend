package com.project.tedi.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccomodationService {

	private final AccomodationRepository accRepo;
	
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

	public List<Accomodation> getFiltered(String people, String loc) {
		if ("any".equals(people)) {
			return accRepo.filteredAccomodationsByLocation(loc);
		}
		else if ("any".equals(loc)) {
			return accRepo.filteredAccomodationsByPeople(Integer.parseInt(people));
		}
		else if ("any".equals(people) && "any".equals(loc)) {
			return getAll();
		}
		else {
			return accRepo.filteredAccomodationsByPeopleAndLocation(Integer.parseInt(people), loc);
		}
	}
	
	
	
}
