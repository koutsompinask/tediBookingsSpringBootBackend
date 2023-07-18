package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.model.User;
import com.project.tedi.service.AdminService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {

	private final AdminService adminServ;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(adminServ.getAllUsers());
	}
	
	@GetMapping("/approve/{id}")
	public ResponseEntity<User> approveUser(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK)
				.body(adminServ.approveUser(id));
	}
	
}
