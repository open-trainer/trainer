package com.opentrainer.domain.user;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByRegistrationOption(RegistrationOption.RegistrationType type, String identifier);
}
