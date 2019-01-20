package com.javasampleapproach.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServicesApplication {

	public static void main(String[] args) {

		Subscriber s = new Subscriber();
		s.run();
		System.out.println(s.getMessage());
		SpringApplication.run(RestServicesApplication.class, args);
	}
}