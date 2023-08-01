package com.project.tedi.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.exception.NotLoggedInException;
import com.project.tedi.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final PhotoService photoServ;
	
	public User details(){
		User loggedIn=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loggedIn==null) {
			throw new NotLoggedInException("User not logged in");
		}
		else return loggedIn; 
	}
	
	public void updateDetails(RegisterRequest regReq,Optional<MultipartFile> photo) {
		User loggedIn=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loggedIn==null) {
			throw new NotLoggedInException("User not logged in");
		}
		if (regReq.getEmail()!=null) loggedIn.setEmail(regReq.getEmail());
		if (regReq.getFirstName()!=null) loggedIn.setFirstName(regReq.getEmail());
		if (regReq.getLastName()!=null) loggedIn.setLastName(regReq.getEmail());
		if (regReq.getUsername()!=null) loggedIn.setUsername(regReq.getEmail());
		if(photo.isPresent()) 
		try{
			String prev=loggedIn.getPhotoUrl(); 
			loggedIn.setPhotoUrl(photoServ.savePhoto(photo.get()));
			if (prev!=null) photoServ.deletePhoto(prev); 
		} catch (Exception e) {
			throw e;
		}
	}
}
