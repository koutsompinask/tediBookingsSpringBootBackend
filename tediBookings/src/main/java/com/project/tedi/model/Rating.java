package com.project.tedi.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
	private Accomodation accomodation;
	
	@ManyToOne
	@JoinColumn(name = "guest_id",nullable = false)
	@JsonIncludeProperties("username")
	private User guest;
	
	@Override
	public int hashCode() {
	    return Objects.hash(id, stars, comment);
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (!(obj instanceof Photo)) return false;
	    Rating other = (Rating) obj;
	    return Objects.equals(id, other.id) &&
	           Objects.equals(stars, other.stars) &&
	           Objects.equals(comment, other.comment);
	}
	
}
