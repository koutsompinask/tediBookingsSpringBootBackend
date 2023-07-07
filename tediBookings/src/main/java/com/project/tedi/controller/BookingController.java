package com.project.tedi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.BookingRequest;
import com.project.tedi.model.Booking;
import com.project.tedi.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/book")
public class BookingController {

	private final BookingService bookServ;
	
	@PostMapping("/{id}")
	public ResponseEntity<Booking> book(@PathVariable Long id ,@RequestBody BookingRequest book ){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bookServ.book(id,book));
	}
	
}
