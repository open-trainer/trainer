package com.opentrainer.user.api.dto;

import com.opentrainer.domain.user.RegistrationOption;
import lombok.Data;

@Data
public class LoginRequest {
    private RegistrationOption.RegistrationType registrationType;
    private String identifier;
    private String secret;
}
