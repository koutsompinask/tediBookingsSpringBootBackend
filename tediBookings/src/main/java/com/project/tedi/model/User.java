package com.project.tedi.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table( name = "user" )
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
	
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
	@JsonIgnore
	private String password;
	
	private String photoUrl;
	
	@OneToMany(mappedBy = "owner")
	@JsonIgnore
	private Set<Accomodation> accomodations;
	
	@OneToMany(mappedBy = "guest")
	@JsonIgnore
	private Set<Booking> bookings;
	
	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	private Set<Message> msgSent;
	
	@OneToMany(mappedBy = "receiver")
	@JsonIgnore
	private Set<Message> msgReceived;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@NonNull
	private Boolean approved;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	
}
