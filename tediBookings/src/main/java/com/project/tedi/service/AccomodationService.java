package com.project.tedi.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.PhotoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccomodationService {

	private final AccomodationRepository accRepo;
	private final PhotoRepository photoRepo;
	private final PhotoService photoServ;
	
	@Transactional
	public Accomodation addAcc(Accomodation acc,MultipartFile[] photos) {
		acc.setOwner((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		accRepo.save(acc);
		try {
			savePhotos(photos, acc);
		} catch (Exception e) {
			return acc;
		}
		return acc;
	}
	
	@Transactional
	public Accomodation updateAcc(Accomodation acc,MultipartFile[] photos) {
		Accomodation prevAcc= accRepo.findById(acc.getId()).orElseThrow(
				() -> new TediBookingsException("accomodation with id not found"));
		if (prevAcc.getOwner()!=SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
			return null;
		}
		String[] ignoreProperties = {"id", "owner", "bookings","photos"};
		BeanUtils.copyProperties(acc, prevAcc, ignoreProperties);
		accRepo.save(prevAcc);
		try {
			savePhotos(photos, prevAcc);
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
			List<Photo> photoList=(photoRepo.findByAccomodationId(ac.getId()));
			Set<Photo> photoSet= new HashSet<>();
			for (Photo p : photoList) {
				photoSet.add(p);
			}
			ac.setPhotos(photoSet);
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
		List<Photo> photoList=(photoRepo.findByAccomodationId(id));
		Set<Photo> photoSet= new HashSet<>();
		for (Photo p : photoList) {
			photoSet.add(p);
		}
		acc.setPhotos(photoSet);
		Resource res=photoServ.getPhoto("e6b205b4-23c5-4e1a-acf8-afa82bb5116f.png");
		return acc;
	}
	
}
