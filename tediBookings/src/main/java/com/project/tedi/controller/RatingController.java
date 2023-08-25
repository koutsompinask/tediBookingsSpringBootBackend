package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.model.Rating;
import com.project.tedi.service.RatingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {
	
	private final RatingService ratingServ;
	
	@PostMapping("/{id}")
	public ResponseEntity<String> rateAccomodation(@PathVariable Long id, @RequestBody Rating rating){
		try {
			ratingServ.rateAccomodation(id, rating);
			return ResponseEntity.ok("rating saved successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<Rating>> getAccomodationRatings(@PathVariable Long id){
		return ResponseEntity.ok(ratingServ.getAccomodationRatings(id));
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<List<Rating>> getUserRatings(@PathVariable Long id){
		return ResponseEntity.ok(ratingServ.getHostRatings(id));
	}
	
}
