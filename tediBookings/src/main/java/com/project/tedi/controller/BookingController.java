package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.BookingRequest;
import com.project.tedi.dto.BookingResponce;
import com.project.tedi.exception.TediBookingsException;
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
		try{
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(bookServ.book(id,book));
		} catch (TediBookingsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/getMine")
	public ResponseEntity<List<BookingResponce>> getBookings(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(bookServ.getMine());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id){
		try {
			bookServ.delete(id);
			return ResponseEntity.ok("Booking cancelled!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("error in deleting booking");
		}
			
	}
	
}