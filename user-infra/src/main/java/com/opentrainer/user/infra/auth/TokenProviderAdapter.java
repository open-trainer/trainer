package com.opentrainer.user.infra.auth;

import com.opentrainer.core.auth.TokenProviderPort;
import com.opentrainer.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenProviderAdapter implements TokenProviderPort {
    @Override
    public String generateToken(User user) {
        // Simple token generation for now, could be JWT
        return UUID.randomUUID().toString();
    }
}
