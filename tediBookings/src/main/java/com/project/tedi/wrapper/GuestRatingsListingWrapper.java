package com.project.tedi.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "Guests_Ratings")
public class GuestRatingsListingWrapper {
	private List<GuestRatings> guestRatings;
}
