package com.example.restCall;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@ComponentScan("app")
@SpringBootApplication
public class BatchWorkApplication {

	

	
	public static void main(String[] args) {

		new SpringApplication(BatchWorkApplication.class).run(args);
	}

	
	
	
	
}