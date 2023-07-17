package com.project.tedi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.model.Accomodation;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

	@GetMapping
	public ResponseEntity<Accomodation> hello() {
		Accomodation acc=new Accomodation();
		acc.setName("something");
		return new ResponseEntity<Accomodation>(acc, HttpStatus.ACCEPTED);
	}
}
