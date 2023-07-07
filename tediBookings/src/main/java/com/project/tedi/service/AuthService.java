package com.project.tedi.service;

import java.util.HashMap;
import java.util.Map;

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
				.role(regReq.getRole())
				.firstName(regReq.getFirstName())
				.lastName(regReq.getLastName())
				.build();
		if (user.getRole().equals(Role.HOST) || user.getRole().equals(Role.HOST_AND_RENTER)) user.setApproved(false);
		else user.setApproved(true);
		userRepo.save(user);
		Map<String,Object> roleMap= new HashMap<>();
		roleMap.put("Role", user.getRole().name());
		String jwtToken = jwtService.generateToken(roleMap,user);
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
		Map<String,Object> roleMap= new HashMap<>();
		roleMap.put("Role", user.getRole().name());
		String jwtToken = jwtService.generateToken(roleMap,user);
		return AuthenticationResponce.builder().token(jwtToken).build();
	}
}
