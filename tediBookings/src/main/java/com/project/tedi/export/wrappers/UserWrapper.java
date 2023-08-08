package com.project.tedi.export.wrappers;

import javax.xml.bind.annotation.XmlRootElement;

import com.project.tedi.model.Role;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class UserWrapper {
	private Long id;
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String photoUrl;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@NonNull
	private Boolean approved;
}
