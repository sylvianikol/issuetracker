package com.syn.issuetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class IssuetrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssuetrackerApplication.class, args);
    }

}
