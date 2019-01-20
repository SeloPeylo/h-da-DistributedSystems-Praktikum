package com.javasampleapproach.restservice.controller;

import com.javasampleapproach.restservice.Subscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
	private static Subscriber s;

	@Value("${server.port}")
	String port;

	@RequestMapping(value = "/")
	public String home() {
		s = new Subscriber();
		s.run();
		System.out.println(s.getMessage());
		return "Okay!" + s.getMessage() + "ffd";
	}

	@RequestMapping("/greeting")
	public String hello() {
		return "Hello from a service running at port: " + port + "!";
	}
}