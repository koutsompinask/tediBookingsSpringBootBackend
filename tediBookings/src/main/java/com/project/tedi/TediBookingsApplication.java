package com.project.tedi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.project.tedi.config.SwaggerConfiguration;

@SpringBootApplication
//@Import(SwaggerConfiguration.class)
public class TediBookingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TediBookingsApplication.class, args);
	}

}
