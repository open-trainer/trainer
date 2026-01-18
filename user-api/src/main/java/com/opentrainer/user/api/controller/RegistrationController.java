package com.opentrainer.user.api.controller;

import com.opentrainer.core.user.RegistrationUseCase;
import com.opentrainer.domain.user.User;
import com.opentrainer.user.api.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationUseCase registrationUseCase;

    @PostMapping
    public User register(@RequestBody RegistrationRequest request) {
        return registrationUseCase.register(
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getRegistrationType(),
                request.getIdentifier(),
                request.getSecret()
        );
    }
}
