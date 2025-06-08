package ru.bell.generatorlease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GeneratorLeaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorLeaseApplication.class, args);
    }

}
