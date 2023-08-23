package com.project.tedi.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewAccomodation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JsonIncludeProperties("id")
	@JoinColumn(name = "accomodation_id", nullable = false)
	@EqualsAndHashCode.Exclude
	private Accomodation accomodation;
	
	@ManyToOne
	@JsonIncludeProperties("id")
	@JoinColumn(name = "user_id", nullable = false)
	@EqualsAndHashCode.Exclude
	private User user;

}
