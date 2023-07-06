package com.project.tedi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.tedi.dto.RegisterRequest;
import com.project.tedi.model.User;
import com.project.tedi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepo;
	
	@Transactional
	public void signup(RegisterRequest regReq) {
		User user = new User();
		user.setUsername(regReq.getUsername());
		user.setEmail(regReq.getEmail());
		user.setPassword(passwordEncoder.encode(regReq.getPassword()));
		userRepo.save(user);
	}
}
