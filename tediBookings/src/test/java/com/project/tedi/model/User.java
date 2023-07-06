package com.project.tedi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private long id;
	
	@JsonProperty("firstName")
	@Column( name = "first_name" )
	private String firstName;
	
	@JsonProperty("lastName")
	@Column( name = "last_name" )
	private String lastName;
	
	@JsonProperty("email")
	@Column( name = "email" , nullable = false)
	private String email;
	
	@JsonProperty("username")
	@Column(name = "username",unique = true ,nullable = false)
	private String username;
	
	@JsonProperty("password")
	@Column(name = "password")
	private String password;
	
	@JsonProperty("photoUrl")
	@Column(name = "photo_url")
	private String photoUrl;
}
