package com.opentrainer.user.api.controller;

import com.opentrainer.user.api.dto.ProfileExtractRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/profile")
public class UserProfileController {
    void extractProfile(@RequestBody ProfileExtractRequest request) {

    }
}
