package com.project.tedi.wrapper;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement(name= "Rating")
@Builder
public class RatingWrapper {
	private String accomodationName;
	private Long accomodation_id;
	private int stars;
	private String comment;
}
