package com.example.trainingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrainingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingSystemApplication.class, args);
    }

}
