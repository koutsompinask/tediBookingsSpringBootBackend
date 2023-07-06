package com.project.tedi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table( name = "user" )
public class User {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private long id;

	private String firstName;
	
	private String lastName;
	
	@Column(nullable = false)
	private String email;
	
	@Column(unique = true ,nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	private String photoUrl;
	
}
