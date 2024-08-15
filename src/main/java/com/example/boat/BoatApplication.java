package com.example.boat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
//@RestController
public class BoatApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(BoatApplication.class, args);
	}

 	@Override
    	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        	return builder.sources(BoatApplication.class);
   	}

}
