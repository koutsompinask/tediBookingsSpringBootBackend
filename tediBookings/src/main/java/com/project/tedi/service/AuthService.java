package com.project.tedi.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.tedi.controller.AuthenticationResponce;
import com.project.tedi.dto.LoginRequest;
import com.project.tedi.dto.RefreshTokenRequest;
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
	private final RefreshTokenService refreshService;

	public AuthenticationResponce signup(RegisterRequest regReq) {
		if (userRepo.findByUsername(regReq.getUsername()).orElse(null) != null) {
			throw new UsernameNotFoundException(null);
		}
		User user = User.builder()
				.username(regReq.getUsername())
				.email(regReq.getEmail())
				.password(passwordEncoder.encode(regReq.getPassword()))
				.role(regReq.getRole())
				.firstName(regReq.getFirstName())
				.lastName(regReq.getLastName())
				.build();
		if (Role.HOST.equals(user.getRole())) {
			user.setApproved(false);
			user.setRole(null);
		}
		else if (Role.HOST_AND_RENTER.equals(user.getRole())) {
			user.setApproved(false);
			user.setRole(Role.RENTER);
		}
		else user.setApproved(true);
		userRepo.save(user);
		Map<String,Object> roleMap= new HashMap<>();
		roleMap.put("Role", user.getRole().name());
		String jwtToken = jwtService.generateToken(roleMap,user);
		return AuthenticationResponce.builder().authToken(jwtToken).build();
	}

	public AuthenticationResponce login(LoginRequest loginRequest) {
		User user = userRepo.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> (new UsernameNotFoundException(loginRequest.getUsername())));
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), 
						loginRequest.getPassword()
				)
			);
		Map<String,Object> roleMap= new HashMap<>();
		roleMap.put("Role", user.getRole().name());
		String jwtToken = jwtService.generateToken(roleMap,user);
		return AuthenticationResponce.builder()
				.authToken(jwtToken)
				.username(loginRequest.getUsername())
				.refreshToken(refreshService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(1000*60))
				.build();
	}
	
	// to do 
	public AuthenticationResponce refresh(RefreshTokenRequest refrReq) {
		refreshService.validateRefreshToken(refrReq.getRefreshToken()); 
		User user = userRepo.findByUsername(refrReq.getUsername()).orElseThrow(() -> new UsernameNotFoundException(refrReq.getUsername()));
		String token = jwtService.generateToken(user);
		return AuthenticationResponce.builder()
				.authToken(token)
				.username(refrReq.getUsername())
				.refreshToken(refrReq.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(1000*60*30))
				.build();
	}
	
}
