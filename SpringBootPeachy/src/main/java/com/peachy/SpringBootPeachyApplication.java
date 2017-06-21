package com.peachy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.peachy.dao", "com.peachy.entity", 
											"com.peachy.service", "com.peachy.controllers", 
											"com.peachy.config", "com.peachy.rest", 
											"com.peachy.component"})
public class SpringBootPeachyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPeachyApplication.class, args);
	}
}
