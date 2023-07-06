package com.project.tedi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.LoginRequest;
import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authServ;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthenticationResponce> signup(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok(authServ.signup(registerRequest));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponce> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authServ.login(loginRequest));
	}
}
