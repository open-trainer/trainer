package com.opentrainer.core.auth;

import com.opentrainer.domain.user.User;

public interface TokenProviderPort {
    String generateToken(User user);
}
