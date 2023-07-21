package com.project.tedi.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponce {
	private Long id;
	private Date from;
	private Date to;
	private String accName;
	private Long accId;
}
