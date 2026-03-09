package com.opentrainer.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.opentrainer.garmin.GarminConnectClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/v1/garmin/login")
public class GarminLoginController {
    @PostMapping
    public void login() {
        // TODO REMOVE
        var client = GarminConnectClient.fromLoginPassword(
                "test@mail.com",
                "testpassword"
        );
        var activities = client.getActivities(10, 0);
        activities.forEach(System.out::println);

    }
}