package com.opentrainer.api;

import org.opentrainer.garmin.config.GarminAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GarminAutoConfiguration.class)
public class TrainingApiMain {
    public static void main(String[] args) {
        SpringApplication.run(TrainingApiMain.class, args);
    }
}