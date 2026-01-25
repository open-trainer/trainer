package com.opentrainer.user.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.opentrainer")
public class UserApiMain {
    public static void main(String[] args) {
        SpringApplication.run(UserApiMain.class, args);
    }
}
