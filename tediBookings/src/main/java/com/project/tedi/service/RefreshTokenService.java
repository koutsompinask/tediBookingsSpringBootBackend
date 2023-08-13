package com.project.tedi.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	
	private static final String SECRET_KEY = "YFXUTiY8wtQmoyEk+hsDweG18O6Ia7kpRRjdjtRHVZP6TYGw3G59SLb64LCpMw7z";
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build().parseClaimsJws(token).getBody();
	}
	
	public String generateRefreshToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).
				setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public boolean validateRefreshToken(String token,String username) {
		return ( extractUsername(token).equals(username) && !extractExpiration(token).before(new Date()) );
	}
	
}
