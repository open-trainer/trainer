package com.opentrainer.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOption {
    private RegistrationType type;
    private String identifier; // email, telegram_id, phone_number
    private String secret;     // hashed password, or token for OAuth
    private LocalDateTime createdAt;

    public enum RegistrationType {
        LOGIN_PASSWORD,
        TELEGRAM,
        GMAIL,
        PHONE
    }
}
