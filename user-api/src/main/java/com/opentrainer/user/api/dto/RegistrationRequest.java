package com.opentrainer.user.api.dto;

import com.opentrainer.domain.user.RegistrationOption;
import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String firstName;
    private String lastName;
    private RegistrationOption.RegistrationType registrationType;
    private String identifier;
    private String secret;
}
