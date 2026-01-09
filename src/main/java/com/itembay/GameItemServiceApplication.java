package com.itembay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GameItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameItemServiceApplication.class, args);
    }
}
