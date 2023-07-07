package com.project.tedi.model;

import java.util.Set;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	@NonNull
	private int floor;
	
	@NonNull
	private int price;
	
	@NonNull
	private int size;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	private AccType type;
	
	@NonNull
	@Lob
	private String description;

	private boolean wifi;
	private boolean heat;
	private boolean kitchen;
	private boolean tv;
	private boolean parking;
	private boolean elevator;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User owner;
	
	@OneToMany(mappedBy = "accomodation")
	private Set<Bookings> bookings;
	
}
