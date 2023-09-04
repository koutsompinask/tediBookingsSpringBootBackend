package com.project.tedi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Booking;
import com.project.tedi.model.UserSearch;
import com.project.tedi.model.UserViewAccomodation;
import com.project.tedi.wrapper.AccomodationListingWrapper;
import com.project.tedi.wrapper.BookingListingWrapper;
import com.project.tedi.wrapper.BookingWrapper;
import com.project.tedi.wrapper.GuestRatings;
import com.project.tedi.wrapper.GuestRatingsListingWrapper;
import com.project.tedi.wrapper.HostRatings;
import com.project.tedi.wrapper.HostRatingsListingWrapper;
import com.project.tedi.wrapper.RatingWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

@Configuration
public class JAXBConfig {

	private JAXBContext jaxbContext;

    @Bean
    public JAXBContext jaxbContext() throws JAXBException {
        if (jaxbContext == null) {
            jaxbContext = JAXBContextFactory.createContext(new Class[]{
            		Accomodation.class, 
        			Booking.class, 
        			BookingWrapper.class,
        			GuestRatings.class,
        			HostRatings.class,
        			RatingWrapper.class,
        			AccomodationListingWrapper.class,
        			BookingListingWrapper.class,
        			GuestRatingsListingWrapper.class,
        			HostRatingsListingWrapper.class,
        			UserSearch.class,
        			UserViewAccomodation.class
                }, null);
        }
        return jaxbContext;
    }
}