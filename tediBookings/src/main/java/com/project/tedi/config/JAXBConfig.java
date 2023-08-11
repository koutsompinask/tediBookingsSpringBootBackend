package com.project.tedi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Booking;
import com.project.tedi.wrapper.BookingWrapper;

import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

@Configuration
public class JAXBConfig {

    @Bean
    public JAXBContext jaxbContext() throws Exception {
        return JAXBContextFactory.createContext(new Class[] {Accomodation.class, Booking.class, BookingWrapper.class}, null);
    }
}