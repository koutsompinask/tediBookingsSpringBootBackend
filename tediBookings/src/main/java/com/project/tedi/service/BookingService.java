package com.project.tedi.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.tedi.dto.BookingRequest;
import com.project.tedi.dto.BookingResponce;
import com.project.tedi.exception.TediBookingsException;
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
		if (!(bookRepo.checkBooked(acc.getId(),from,to)).isEmpty()) {
			throw new TediBookingsException("Anavailable accomodation");
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Booking book= null;		
		book = Booking.builder()
				.accomodation(acc)
				.fromDate(from)
				.toDate(to)
				.guest(user)
				.build();
		bookRepo.save(book);
		return book;
	}

	public List<BookingResponce> getMine() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Booking> bookings= bookRepo.findByUser(user.getId());
		List<BookingResponce> res = new ArrayList<>();
		for (Booking b : bookings) {
			res.add(BookingResponce.builder().accId(b.getAccomodation().getId())
					.id(b.getId())
					.accName(b.getAccomodation().getName())
					.from(b.getFromDate())
					.to(b.getToDate())
					.build());
		}
		return res;
	}

}
