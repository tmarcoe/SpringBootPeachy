package com.peachy;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.peachy.dao", "com.peachy.entity", 
											"com.peachy.service", "com.peachy.controllers", 
											"com.peachy.config", "com.peachy.rest", 
											"com.peachy.component"})
public class SpringBootPeachyApplication implements CommandLineRunner {
	
    @Autowired
    DataSource dataSource;


	public static void main(String[] args) {
		SpringApplication.run(SpringBootPeachyApplication.class, args);
		//System.out.println("System has started");
	}
	
	@Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);

    }
}
