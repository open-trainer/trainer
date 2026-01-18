package com.opentrainer.core.user;

import com.opentrainer.domain.user.RegistrationOption;
import com.opentrainer.domain.user.User;
import com.opentrainer.domain.user.UserRepository;
import com.opentrainer.core.auth.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RegistrationUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public User register(String email, String firstName, String lastName, RegistrationOption.RegistrationType type, String identifier, String rawSecret) {
        if (userRepository.findByRegistrationOption(type, identifier).isPresent()) {
            throw new RuntimeException("User already exists with this registration option");
        }

        String encodedSecret = (type == RegistrationOption.RegistrationType.LOGIN_PASSWORD && rawSecret != null) 
                ? passwordEncoder.encode(rawSecret) 
                : rawSecret;

        User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .registrationOptions(new ArrayList<>())
                .active(true)
                .build());

        RegistrationOption option = RegistrationOption.builder()
                .type(type)
                .identifier(identifier)
                .secret(encodedSecret)
                .createdAt(LocalDateTime.now())
                .build();

        user.addRegistrationOption(option);
        return userRepository.save(user);
    }
}
