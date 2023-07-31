package com.project.tedi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.dto.SearchRequest;
import com.project.tedi.model.Accomodation;
import com.project.tedi.service.AccomodationService;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/accomodation")
@AllArgsConstructor
public class AccomodationController {
	
	private AccomodationService accomServe;

	@PostMapping(value = "/enlist", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public ResponseEntity<Accomodation> enlist(@RequestPart("accomodation") Accomodation acc, @RequestPart("photos") Optional<MultipartFile[]> photos){
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accomServe.addAcc(acc,photos));
	}
	
	@PutMapping(value = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public ResponseEntity<Accomodation> update(@PathVariable("id") Long id ,@RequestPart("accomodation") Accomodation acc, @RequestPart("photos") Optional<MultipartFile[]> photos){
		Accomodation retAcc = accomServe.updateAcc(acc,photos,id);
		ResponseEntity<Accomodation> ret;
		if (retAcc == null) ret= ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		else ret = ResponseEntity.status(HttpStatus.CREATED).body(retAcc);
		return ret;
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Accomodation>> getAll(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getAll());
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Accomodation> getById(@PathVariable("id") Long id){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getById(id));
	}
	
	@GetMapping("/getMine")
	public ResponseEntity<List<Accomodation>> getByOwner(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getByOwner());
	}
	
	@PostMapping("/getFiltered")
	public ResponseEntity<List<Accomodation>> getFiltered(@RequestBody SearchRequest searchreq){
		return ResponseEntity.status(HttpStatus.OK)
				.body(accomServe.getFiltered(searchreq));
	}

}