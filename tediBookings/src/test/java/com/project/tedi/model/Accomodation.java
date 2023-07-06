package com.project.tedi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "accomodation")
public class Accomodation {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	Long id;
	
}
