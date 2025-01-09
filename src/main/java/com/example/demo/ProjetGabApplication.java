package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.demo.entity")  // Si tu as des entités, sinon laisse-le de côté
public class ProjetGabApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetGabApplication.class, args);
    }

}
