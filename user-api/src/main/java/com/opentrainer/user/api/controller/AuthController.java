package com.opentrainer.user.api.controller;

import com.opentrainer.core.user.LoginUseCase;
import com.opentrainer.user.api.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = loginUseCase.login(
                request.getRegistrationType(),
                request.getIdentifier(),
                request.getSecret()
        );
        return Map.of("token", token);
    }
}
