package com.example.pitdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for PIT Mutation Testing Demo.
 * 
 * This class is excluded from mutation testing as it's a simple bootstrapping class
 * with no business logic to test.
 */
@SpringBootApplication
public class PitDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PitDemoApplication.class, args);
    }
}
