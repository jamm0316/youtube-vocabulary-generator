package com.personalproject.llmmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LlmClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmClientApplication.class, args);
	}

}
