package com.project.tedi.dto;

import com.project.tedi.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String phone;
	private Role role;
}