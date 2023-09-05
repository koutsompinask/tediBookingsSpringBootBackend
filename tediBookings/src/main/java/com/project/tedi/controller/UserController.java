package com.project.tedi.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.exception.NotLoggedInException;
import com.project.tedi.model.User;
import com.project.tedi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userServ;
	
	@GetMapping("/details")
	public ResponseEntity<User> getDetails(){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(userServ.details());
		} catch(NotLoggedInException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> updateDetails(@RequestPart("regReq") RegisterRequest regReq, @RequestPart("photo") Optional<MultipartFile> photo){
		try {
			userServ.updateDetails(regReq, photo);
			return ResponseEntity.status(HttpStatus.OK).body("user details updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/changePass")
	public ResponseEntity<String> changePassword(@RequestBody String newPassword){
		try {
			userServ.changePassword(newPassword);
			return ResponseEntity.status(HttpStatus.OK).body("user password updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}