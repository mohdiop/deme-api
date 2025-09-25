package com.mohdiop.deme_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableScheduling
@EnableMethodSecurity
@SpringBootApplication
public class DemeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemeApiApplication.class, args);
    }

}
