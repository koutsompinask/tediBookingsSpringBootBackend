//package com.project.tedi;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.project.tedi.model.User;
//
//import javax.xml.bind.JAXBContext;
//
//import org.eclipse.persistence.jaxb.JAXBContextFactory;
//
//@Configuration
//public class JAXBConfig {
//
//    @Bean
//    public JAXBContext jaxbContext() throws Exception {
//        return JAXBContextFactory.createContext(new Class[] { User.class }, null);
//    }
//}