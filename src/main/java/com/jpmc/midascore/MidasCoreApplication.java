package com.jpmc.midascore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // <-- ADD THIS
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate; // <-- ADD THIS

@EnableKafka
@SpringBootApplication
public class MidasCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    // ADD THIS BEAN FOR TASK 4
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}