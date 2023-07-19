package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.SearchRequest;
import com.project.tedi.model.Accomodation;
import com.project.tedi.service.AccomodationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/accomodation")
@AllArgsConstructor
public class AccomodationController {
	
	private AccomodationService accomServe;

	@PostMapping("/enlist")
	public ResponseEntity<Accomodation> enlist(@RequestBody Accomodation acc){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accomServe.addAcc(acc));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Accomodation>> getAll(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getAll());
	}
	
//	@GetMapping("/get/p={people}/loc={loc}")
//	public ResponseEntity<List<Accomodation>> getFiltered(@PathVariable String people , @PathVariable String loc){
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(accomServe.getFiltered(people,loc));
//		
//	}
	
	@PostMapping("/getFiltered")
	public ResponseEntity<List<Accomodation>> getFiltered(@RequestBody SearchRequest searchreq){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getFiltered(searchreq));
		
	}
}
