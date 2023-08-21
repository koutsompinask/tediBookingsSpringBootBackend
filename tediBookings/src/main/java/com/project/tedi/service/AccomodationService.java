package com.project.tedi.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.SearchRequest;
import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Photo;
import com.project.tedi.model.Rating;
import com.project.tedi.model.User;
import com.project.tedi.model.UserSearch;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.PhotoRepository;
import com.project.tedi.repository.UserSearchRepository;

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
		for (Accomodation ac : accFiltered) {
			List<Rating> rateList = rateServ.getAccomodationRatings(ac.getId());
			for (Rating r : rateList) r.setAccomodation(null);
			ac.setRatings(Set.copyOf(rateList));
			ac.setPhotos(Set.copyOf((photoRepo.findByAccomodationId(ac.getId()))));
		}
		User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		if (loggedIn != null) {
			userSearchRepo.save(UserSearch.builder().location(loc).numPerson(people).user(loggedIn).build());
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
		Set<Photo> seP = Set.copyOf(photoRepo.findByAccomodationId(id));
		if (!seP.isEmpty()) acc.setPhotos(seP);
		return acc;
	}
	
}
