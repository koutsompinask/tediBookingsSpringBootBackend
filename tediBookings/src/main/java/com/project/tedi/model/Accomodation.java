package com.project.tedi.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "accomodation" )
public class Accomodation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NonNull
	private String name;
	
	@NonNull
	private String location;
	
	private float lat;
	
	private float lng;
	
	@Lob
	private String transportation; 
	
	@NonNull
	private int floor;
	
	@NonNull
	private int price;
	
	@NonNull
	private int extraCost;
	
	@NonNull
	private int size;
	
	@NonNull
	private int beds;
	
	@NonNull
	private int rooms;
	
	@NonNull
	private int bathrooms;
	
	@NonNull
	private int maxPerson;
	
	@NonNull
	private Date availableFrom;
	
	@NonNull
	private Date availableTo;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	private AccType type;
	
	@NonNull
	@Lob
	private String description;

	private boolean sittingRoom;
	private boolean wifi;
	private boolean heat;
	private boolean kitchen;
	private boolean tv;
	private boolean parking;
	private boolean elevator;
	
	@ManyToOne
	@JsonIncludeProperties({"firstName","lastName"})
	@JoinColumn(name = "user_id", nullable = false)
	private User owner;
	
	@OneToMany(mappedBy = "accomodation")
	@JsonIgnore
	private Set<Booking> bookings;
	
	@OneToMany(mappedBy ="accomodation")
	@JsonIncludeProperties("filename")
	private Set<Photo> photos;
}
