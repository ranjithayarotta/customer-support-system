package com.example.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.example.jwt"
})
public class TickerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TickerServiceApplication.class, args);
    }
}
