package com.project.tedi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recommend")
@RequiredArgsConstructor
public class RecommendationController {
	
	private final RecommendationService recServ;

	@GetMapping("/{id}")
	public ResponseEntity<String> recommend(@PathVariable("id") long id){
		recServ.recommend();
		return ResponseEntity.ok("ok");
	}
}
