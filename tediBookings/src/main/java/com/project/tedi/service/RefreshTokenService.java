package com.project.tedi.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.tedi.exception.TediBookingsException;
import com.project.tedi.model.RefreshToken;
import com.project.tedi.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		return refreshTokenRepository.save(refreshToken);
	}
	
	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new TediBookingsException("invalid refresh token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
	
}
