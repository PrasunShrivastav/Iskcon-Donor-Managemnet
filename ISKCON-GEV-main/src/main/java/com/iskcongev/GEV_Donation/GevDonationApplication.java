package com.iskcongev.GEV_Donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class GevDonationApplication {
	@RequestMapping("/")
	String home(){
		return "Hello World.";
	}
	public String requestMethodName(@RequestParam String param) {
		return new String();
	}
	

	public static void main(String[] args) {
		SpringApplication.run(GevDonationApplication.class, args);
	}

}
