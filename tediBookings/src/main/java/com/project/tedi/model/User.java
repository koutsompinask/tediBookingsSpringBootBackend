package com.project.tedi.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	private String password;
	
	private String photoUrl;
	
	@OneToOne(mappedBy = "owner")
	private Accomodation accomodation;
	
	@OneToMany(mappedBy = "guest")
	private Set<Bookings> bookings;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@NonNull
	private Boolean approved;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
