package com.project.tedi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.tedi.dto.MessageRequest;
import com.project.tedi.model.Message;
import com.project.tedi.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
public class MessageController {
	
	private final MessageService msgServ;

	@PostMapping("/send")
	public ResponseEntity<String> sendMessage(@RequestBody MessageRequest msgReq) {
		try {
			msgServ.sendMessage(msgReq);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("error in sending message %s",e.getMessage()));
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("message sent successfully");
	}
	
	@GetMapping("/getInbox")
	public ResponseEntity<List<Message>> getInbox(){
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(msgServ.getUserMessages());
		} catch (Exception e){ 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
	}
}
