package com.project.tedi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.LoginRequest;
import com.project.tedi.dto.RefreshTokenRequest;
import com.project.tedi.dto.RegisterRequest;
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
	public ResponseEntity<AuthenticationResponce> signup(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok(authServ.signup(registerRequest));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponce> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authServ.login(loginRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponce> refresh(@Validated @RequestBody RefreshTokenRequest refreshRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(authServ.refresh(refreshRequest));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Validated @RequestBody RefreshTokenRequest refrReq){
		refrServ.deleteRefreshToken(refrReq.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK)
				.body("Token deleted successfuly");
	}
}
