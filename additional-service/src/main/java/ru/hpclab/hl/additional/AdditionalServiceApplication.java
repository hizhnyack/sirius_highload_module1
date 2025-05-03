package ru.hpclab.hl.additional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdditionalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdditionalServiceApplication.class, args);
    }
} 