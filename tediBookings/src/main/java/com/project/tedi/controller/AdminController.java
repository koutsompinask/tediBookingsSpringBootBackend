package com.project.tedi.controller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.tedi.export.wrappers.UserWrapper;
import com.project.tedi.model.User;
import com.project.tedi.service.AdminService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {

	private final AdminService adminServ;
	private final JAXBContext jaxbContext;
	
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
	
	@GetMapping("/download/users/json")
    public ResponseEntity<String> downloadUsersJson() {
        List<User> userList = adminServ.getAllUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			String jsonData = objectMapper.writeValueAsString(userList);
			return ResponseEntity.ok(jsonData);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}
    }
	
	@GetMapping("/download/users/xml")
    public ResponseEntity<String> downloadUsersXml() {
        List<User> userList = adminServ.getAllUsers();
        List<UserWrapper> userWrapperList=convertToUserXmlWrapper(userList);
        Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(userWrapperList, stringWriter);
			return ResponseEntity.ok(stringWriter.toString());
		} catch (JAXBException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

    }
	
	public static List<UserWrapper> convertToUserXmlWrapper(List<User> users){
		List<UserWrapper> retList = new ArrayList<>();
		for (User u : users) {
			UserWrapper uw = UserWrapper.builder()
					.id(u.getId())
					.firstName(u.getFirstName())
					.lastName(u.getLastName())
					.email(u.getEmail())
					.photoUrl(u.getPhotoUrl())
					.role(u.getRole()).build();
			retList.add(uw);
		}
		return retList;
	}
	
}
