package com.project.tedi.controller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Booking;
import com.project.tedi.model.Rating;
import com.project.tedi.model.User;
import com.project.tedi.service.AdminService;
import com.project.tedi.wrapper.AccomodationListingWrapper;
import com.project.tedi.wrapper.BookingListingWrapper;
import com.project.tedi.wrapper.BookingWrapper;
import com.project.tedi.wrapper.GuestRatings;
import com.project.tedi.wrapper.GuestRatingsListingWrapper;
import com.project.tedi.wrapper.HostRatings;
import com.project.tedi.wrapper.HostRatingsListingWrapper;
import com.project.tedi.wrapper.RatingWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {

	private final AdminService adminServ;
	private final JAXBContext jaxbContext;
	private static final String[] accomodationIgnoreProps= {"owner","bookings","photos","ratings","searched","viewed"};
	private static final String[] bookingIgnoreProps = {"guest","accomodation"};
	private static final String[] userIgnoreProps = {"accomodations","bookings","msgSent","msgReceived","ratingsPosted"};
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(adminServ.getAllUsersForApproval());
	}
	
	@GetMapping("/approve/{id}")
	public ResponseEntity<User> approveUser(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK)
				.body(adminServ.approveUser(id));
	}
	
	@GetMapping("/download/accomodations/json")
    public ResponseEntity<String> downloadAccomodationJson() {
        List<Accomodation> accList = adminServ.getAllAccomodations();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String jsonData = objectMapper.writeValueAsString(accList);
			return ResponseEntity.ok(jsonData);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}
    }
	
	@GetMapping("/download/accomodations/xml")
    public ResponseEntity<String> downloadAccomodationsXml() {
        List<Accomodation> accList = adminServ.getAllAccomodations();
        List<Accomodation> accWrapList = new ArrayList<>();
        for (Accomodation a : accList) {
        	Accomodation aWrap=new Accomodation();
        	BeanUtils.copyProperties(a, aWrap, accomodationIgnoreProps);
        	accWrapList.add(aWrap);
        }
        Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(new AccomodationListingWrapper(accWrapList), stringWriter);
			return ResponseEntity.ok(stringWriter.toString());
		} catch (JAXBException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
	
	@GetMapping("/download/bookings/json")
    public ResponseEntity<String> downloadBookingsJson() {
        List<Booking> bookList = adminServ.getAllBookings();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String jsonData = objectMapper.writeValueAsString(bookList);
			return ResponseEntity.ok(jsonData);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}
    }
	
	@GetMapping("/download/bookings/xml")
    public ResponseEntity<String> downloadBookingsXml() {
        List<Booking> bookList = adminServ.getAllBookings();
        List<BookingWrapper> bookWrapList = new ArrayList<>();
        for (Booking b : bookList) {
        	BookingWrapper bWrap=BookingWrapper.builder().
        			id(b.getId())
        			.accomodationName(b.getAccomodation().getName())
        			.guestUserName(b.getGuest().getUsername())
        			.from(b.getFromDate())
        			.to(b.getToDate()).build();        			
        	bookWrapList.add(bWrap);
        } 
        Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(new BookingListingWrapper(bookWrapList), stringWriter);
			return ResponseEntity.ok(stringWriter.toString());
		} catch (JAXBException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
	
	@GetMapping("/download/ratings/guest/json")
    public ResponseEntity<String> downloadGuestRatingsJson() {
        List<User> userList = adminServ.getAllRenters();
        List<GuestRatings> guestRatings= new ArrayList<>();
        for (User u : userList) {
        	List<Rating> uRatings = adminServ.getAllRatingsByGuest(u.getId());
        	if (uRatings == null || uRatings.isEmpty()) continue;
        	Set<RatingWrapper> uRatingsWrap = new HashSet<>();
        	for (Rating r : uRatings) {
        		RatingWrapper rWrap = RatingWrapper.builder()
        								.accomodation_id(r.getAccomodation().getId())
        								.accomodationName(r.getAccomodation().getName())
        								.stars(r.getStars())
        								.comment(r.getComment())
        								.build();
        		uRatingsWrap.add(rWrap);
        	}
        	GuestRatings temp = GuestRatings.builder()
        						.guestId(u.getId())
        						.username(u.getUsername())
        						.ratings(uRatingsWrap)
        						.build();
        	guestRatings.add(temp);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String jsonData = objectMapper.writeValueAsString(guestRatings);
			return ResponseEntity.ok(jsonData);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}
    }
	
	@GetMapping("/download/ratings/guest/xml")
    public ResponseEntity<String> downloadGuestRatingsXml() {
		List<User> userList = adminServ.getAllRenters();
        List<GuestRatings> guestRatings= new ArrayList<>();
        for (User u : userList) {
        	List<Rating> uRatings = adminServ.getAllRatingsByGuest(u.getId());
        	if (uRatings == null || uRatings.isEmpty()) continue;
        	Set<RatingWrapper> uRatingsWrap = new HashSet<>();
        	for (Rating r : uRatings) {
        		RatingWrapper rWrap = RatingWrapper.builder()
        								.accomodation_id(r.getAccomodation().getId())
        								.accomodationName(r.getAccomodation().getName())
        								.stars(r.getStars())
        								.comment(r.getComment())
        								.build();
        		uRatingsWrap.add(rWrap);
        	}
        	GuestRatings temp = GuestRatings.builder()
        						.guestId(u.getId())
        						.username(u.getUsername())
        						.ratings(uRatingsWrap)
        						.build();
        	guestRatings.add(temp);
        }
        Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(new GuestRatingsListingWrapper(guestRatings), stringWriter);
			return ResponseEntity.ok(stringWriter.toString());
		} catch (JAXBException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
	
	@GetMapping("/download/ratings/host/json")
    public ResponseEntity<String> downloadHostRatingsJson() {
        List<User> userList = adminServ.getAllHosts();
        List<HostRatings> guestRatings= new ArrayList<>();
        for (User u : userList) {
        	List<Rating> uRatings = adminServ.getAllRatingsByHost(u.getId());
        	if (uRatings == null || uRatings.isEmpty()) continue;
        	Set<RatingWrapper> uRatingsWrap = new HashSet<>();
        	for (Rating r : uRatings) {
        		RatingWrapper rWrap = RatingWrapper.builder()
        								.accomodation_id(r.getAccomodation().getId())
        								.accomodationName(r.getAccomodation().getName())
        								.stars(r.getStars())
        								.comment(r.getComment())
        								.build();
        		uRatingsWrap.add(rWrap);
        	}
        	HostRatings temp = HostRatings.builder()
        						.hostId(u.getId())
        						.username(u.getUsername())
        						.ratings(uRatingsWrap)
        						.build();
        	guestRatings.add(temp);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String jsonData = objectMapper.writeValueAsString(guestRatings);
			return ResponseEntity.ok(jsonData);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}
    }
	
	@GetMapping("/download/ratings/host/xml")
    public ResponseEntity<String> downloadHostRatingsXml() {
		List<User> userList = adminServ.getAllHosts();
        List<HostRatings> guestRatings= new ArrayList<>();
        for (User u : userList) {
        	List<Rating> uRatings = adminServ.getAllRatingsByHost(u.getId());
        	if (uRatings == null || uRatings.isEmpty()) continue;
        	Set<RatingWrapper> uRatingsWrap = new HashSet<>();
        	for (Rating r : uRatings) {
        		RatingWrapper rWrap = RatingWrapper.builder()
        								.accomodation_id(r.getAccomodation().getId())
        								.accomodationName(r.getAccomodation().getName())
        								.stars(r.getStars())
        								.comment(r.getComment())
        								.build();
        		uRatingsWrap.add(rWrap);
        	}
        	HostRatings temp = HostRatings.builder()
        						.hostId(u.getId())
        						.username(u.getUsername())
        						.ratings(uRatingsWrap)
        						.build();
        	guestRatings.add(temp);
        }
        Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(new HostRatingsListingWrapper(guestRatings), stringWriter);
			return ResponseEntity.ok(stringWriter.toString());
		} catch (JAXBException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
	
}
