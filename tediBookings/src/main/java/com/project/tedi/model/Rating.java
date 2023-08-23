package com.project.tedi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rating")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	private int stars;
	
	@Lob
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "accomodation_id",nullable = false)
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Accomodation accomodation;
	
	@ManyToOne
	@JoinColumn(name = "guest_id",nullable = false)
	@JsonIncludeProperties("username")
	@EqualsAndHashCode.Exclude
	private User guest;
	
}
