package com.project.tedi.wrapper;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name =  "Booking")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingWrapper {
	private Long id;
	private String guestUserName;
	private String accomodationName;
	private Date from;
	private Date to;
}
