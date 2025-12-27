package com.opentrainer.user.api.controller;

import com.opentrainer.user.api.dto.GarminIdentity;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/garmin")
public class GarminAuthorizationController {
    void authorize(GarminIdentity identity) {}
}
