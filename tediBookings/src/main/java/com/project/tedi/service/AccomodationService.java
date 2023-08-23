package com.project.tedi.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.SearchRequest;
import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Photo;
import com.project.tedi.model.Role;
import com.project.tedi.model.User;
import com.project.tedi.model.UserSearch;
import com.project.tedi.model.UserViewAccomodation;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.PhotoRepository;
import com.project.tedi.repository.UserSearchRepository;
import com.project.tedi.repository.UserViewAccomodationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccomodationService {

	private final AccomodationRepository accRepo;
	private final PhotoRepository photoRepo;
	private final PhotoService photoServ;
	private final RatingService rateServ;
	private final UserSearchRepository userSearchRepo;
	private final UserViewAccomodationRepository userViewAccRepo;
	
	@Transactional
	public Accomodation addAcc(Accomodation acc,Optional<MultipartFile[]> photos) {
		acc.setOwner((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		accRepo.save(acc);
		try {
			if (photos.isPresent()) {
				savePhotos(photos.get(), acc);
			}
		} catch (Exception e) {
			return acc;
		}
		return acc;
	}
	
	@Transactional
	public Accomodation updateAcc(Accomodation acc,Optional<MultipartFile[]> photos,Long id) {
		Accomodation prevAcc= accRepo.findById(id).orElseThrow(
				() -> new TediBookingsException("accomodation with id not found"));
		User loggedIn= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (prevAcc.getOwner().getId()!=loggedIn.getId()) {
			return null;
		}
		String[] ignoreProperties = {"id", "owner", "bookings","photos"};
		BeanUtils.copyProperties(acc, prevAcc, ignoreProperties);
		accRepo.save(prevAcc);
		try {
			if (photos.isPresent()) {
				savePhotos(photos.get(), prevAcc);
			}
		} catch (Exception e) {
			return prevAcc;
		}
		return prevAcc;
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
		List<Accomodation> accFiltered=accRepo.filteredAccomodations(people, loc,from,to);
		User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for (Accomodation ac : accFiltered) {
			if (loggedIn != null && (loggedIn.getRole()==Role.RENTER ||loggedIn.getRole()==Role.HOST_AND_RENTER)) {
				userSearchRepo.save(UserSearch.builder().accomodation(ac).user(loggedIn).build());
			}
			ac.setRatings(Set.copyOf(rateServ.getAccomodationRatings(ac.getId())));
			ac.setPhotos(Set.copyOf((photoRepo.findByAccomodationId(ac.getId()))));
		}
		return accFiltered;	
	}
	
	public List<Accomodation> getByOwner(){
		User owner= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return accRepo.findByOwnerId(owner.getId());
	}
	
	public void savePhotos(MultipartFile[] photos,Accomodation acc) throws IOException {
		for (MultipartFile file : photos) {
			Photo p = Photo.builder().filename(photoServ.savePhoto(file))
						.accomodation(acc).build();
			photoRepo.save(p);
		}
		return;
	}

	public Accomodation getById(Long id) {
		Accomodation acc = accRepo.findById(id).orElseThrow(
				()-> new TediBookingsException("acc not found"));
		User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loggedIn != null && (loggedIn.getRole()==Role.RENTER ||loggedIn.getRole()==Role.HOST_AND_RENTER)) {
			userViewAccRepo.save(UserViewAccomodation.builder().accomodation(acc).user(loggedIn).build());
		}
		Set<Photo> seP = Set.copyOf(photoRepo.findByAccomodationId(id));
		if (!seP.isEmpty()) acc.setPhotos(seP);
		return acc;
	}
	
}
