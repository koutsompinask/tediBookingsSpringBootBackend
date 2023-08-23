package com.project.tedi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIncludeProperties({"firstName","lastName"})
	@EqualsAndHashCode.Exclude
	private User guest;
	
	@ManyToOne
	@JoinColumn(name = "accomodation_id")
	@JsonIncludeProperties("name")
	@EqualsAndHashCode.Exclude
	private Accomodation accomodation;
	
	@NonNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date fromDate;
	
	@NonNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date toDate;
	
}
