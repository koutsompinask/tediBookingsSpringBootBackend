package com.project.tedi.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.tedi.dto.MessageRequest;
import com.project.tedi.model.Message;
import com.project.tedi.model.User;
import com.project.tedi.repository.MessageRepository;
import com.project.tedi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

	private final MessageRepository messageRepo;
	private final UserRepository userRepo;
	
	public void sendMessage(MessageRequest mess) {
		User sender =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User receiver = userRepo.findById(mess.getReceiverId()).orElseThrow(
				()-> new UsernameNotFoundException(String.format("User with id %ld not found", mess.getReceiverId())));
		Message msg=Message.builder()
			.message(mess.getMessage())
			.sender(sender)
			.receiver(receiver)
			.timestamp(new Date())
			.build();
		try {
			messageRepo.save(msg);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Message> getUserMessages(){
		User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user==null) {
			throw new RuntimeException("you are not logged in");
		}
		return messageRepo.getUserInbox(user.getId());
	}
}
