package com.project.tedi.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.AuthenticationResponce;
import com.project.tedi.dto.LoginRequest;
import com.project.tedi.dto.RefreshTokenRequest;
import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.service.AuthService;
import com.project.tedi.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authServ;
	private final RefreshTokenService refrServ;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthenticationResponce> signup(@RequestPart("user") RegisterRequest registerRequest,@RequestPart("photo") Optional<MultipartFile> photo) {
		return ResponseEntity.ok(authServ.signup(registerRequest,photo));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponce> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authServ.login(loginRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponce> refresh(@Validated @RequestBody RefreshTokenRequest refreshRequest) {
		try{
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(authServ.refresh(refreshRequest));
		} catch (TediBookingsException e){
			return ResponseEntity.status(HttpStatus.GONE).body(null);
		}
	}
	
}
