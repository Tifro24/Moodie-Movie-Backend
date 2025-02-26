package com.pilot.sakila;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SakilaRestApiApplication {

	@PostConstruct
	public void init() {
		System.out.println("✅ SakilaController loaded!");  // This should print in logs
	}

	public static void main(String[] args) {
		SpringApplication.run(SakilaRestApiApplication.class, args);
	}

}
