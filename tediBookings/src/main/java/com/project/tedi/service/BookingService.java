package com.project.tedi.service;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.dto.BookingRequest;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Booking;
import com.project.tedi.model.User;
import com.project.tedi.repository.AccomodationRepository;
import com.project.tedi.repository.BookingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
	
	private final BookingsRepository bookRepo;
	private final AccomodationRepository accRepo;
	
	public Booking book(Long id,BookingRequest b) {
		Date from = b.getFrom();
		Date to = b.getTo();
		Accomodation acc = accRepo.findById(id).orElseThrow();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Booking book = Booking.builder()
			.accomodation(acc)
			.dateFrom(from)
			.dateTo(to)
			.guest(user)
			.build();
		bookRepo.save(book);
		return book;
	}

}
