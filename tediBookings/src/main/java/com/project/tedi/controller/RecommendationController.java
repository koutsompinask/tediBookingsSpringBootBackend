package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.model.Accomodation;
import com.project.tedi.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recommend")
@RequiredArgsConstructor
public class RecommendationController {
	
	private final RecommendationService recServ;

	@GetMapping
	public ResponseEntity<List<Accomodation>> recommend(){
		try{
			return ResponseEntity.ok(recServ.recommend());
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}
