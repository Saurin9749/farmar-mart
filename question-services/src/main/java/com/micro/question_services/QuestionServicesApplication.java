package com.micro.question_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class QuestionServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionServicesApplication.class, args);
	}

}
