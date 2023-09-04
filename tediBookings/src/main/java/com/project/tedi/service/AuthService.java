package com.project.tedi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.AuthenticationResponce;
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
	private final PhotoService photoService;
	
	public void signup(RegisterRequest regReq) {
		signup(regReq,Optional.empty());
	}

	public void signup(RegisterRequest regReq,Optional<MultipartFile> photo) {
		if (userRepo.findByUsername(regReq.getUsername()).orElse(null) != null) {
			throw new TediBookingsException("username already exists");
		}
		User user = User.builder()
				.username(regReq.getUsername())
				.email(regReq.getEmail())
				.phone(regReq.getPhone())
				.password(passwordEncoder.encode(regReq.getPassword()))
				.role(regReq.getRole())
				.firstName(regReq.getFirstName())
				.lastName(regReq.getLastName())
				.build();
		if (Role.HOST.equals(user.getRole())) {
			user.setApproved(false);
			user.setRole(Role.PENDING);
		}
		else if (Role.HOST_AND_RENTER.equals(user.getRole())) {
			user.setApproved(false);
			user.setRole(Role.RENTER);
		}
		else user.setApproved(true);
		if (photo.isPresent()) {
			try {
				user.setPhotoUrl(photoService.savePhoto(photo.get()));
			} catch (Exception e) {
				user.setPhotoUrl(null);
			}
		}
		else user.setPhotoUrl(null);
		userRepo.save(user);
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
				.role(user.getRole().name())
				.id(user.getId())
				.refreshToken(refreshService.generateRefreshToken(user))
				.build();
	}
	
	public AuthenticationResponce refresh(RefreshTokenRequest refrReq) {
		if(refreshService.validateRefreshToken(refrReq.getRefreshToken(),refrReq.getUsername())) {
			User user = userRepo.findByUsername(refrReq.getUsername()).orElseThrow(() -> new UsernameNotFoundException(refrReq.getUsername()));
			String token = jwtService.generateToken(user);
			return AuthenticationResponce.builder()
					.authToken(token)
					.username(refrReq.getUsername())
					.refreshToken(refrReq.getRefreshToken())
					.build();
		} else {
			throw new TediBookingsException("error refreshing token");
		}
	}
	
	public boolean usernameExists(String username) {
		return userRepo.findByUsername(username).isPresent();
	}
	
}
