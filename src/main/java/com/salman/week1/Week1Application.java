package com.salman.week1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Week1Application {

    public static void main(String[] args) {
        SpringApplication.run(Week1Application.class, args);
    }

}
