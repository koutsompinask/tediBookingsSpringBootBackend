package com.project.tedi.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.service.PhotoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/photo")
@RequiredArgsConstructor
public class PhotoController {
	
	private final PhotoService photoServ;

	@GetMapping("/{filename}")
	public ResponseEntity<Resource> getPhoto(@PathVariable("filename") String filename){
		try {			
			return ResponseEntity.ok(photoServ.getPhoto(filename));
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
}
