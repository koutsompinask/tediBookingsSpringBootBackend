package com.project.tedi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.tedi.controller.AuthenticationResponce;
import com.project.tedi.dto.LoginRequest;
import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.model.Role;
import com.project.tedi.model.User;
import com.project.tedi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	private final AuthenticationManager authManager;
	private final JwtService jwtService;

	public AuthenticationResponce signup(RegisterRequest regReq) {
		User user = User.builder()
				.username(regReq.getUsername())
				.email(regReq.getEmail())
				.password(passwordEncoder.encode(regReq.getPassword()))
				.role(Role.RENTER)
				.build();
		userRepo.save(user);
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponce.builder().token(jwtToken).build();
	}

	public AuthenticationResponce login(LoginRequest loginRequest) {
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), 
						loginRequest.getPassword()
				)
			);
		User user = userRepo.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> (new TediBookingsException("username not found")));
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponce.builder().token(jwtToken).build();
	}
}
