package com.opentrainer.core.user;

import com.opentrainer.domain.user.RegistrationOption;
import com.opentrainer.domain.user.User;
import com.opentrainer.domain.user.UserRepository;
import com.opentrainer.core.auth.PasswordEncoderPort;
import com.opentrainer.core.auth.TokenProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    public String login(RegistrationOption.RegistrationType type, String identifier, String secret) {
        Optional<User> userOpt = userRepository.findByRegistrationOption(type, identifier);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid identifier or password");
        }

        User user = userOpt.get();
        RegistrationOption option = user.getRegistrationOptions().stream()
                .filter(o -> o.getType() == type && o.getIdentifier().equals(identifier))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Registration option not found"));

        if (type == RegistrationOption.RegistrationType.LOGIN_PASSWORD) {
            if (!passwordEncoder.matches(secret, option.getSecret())) {
                throw new RuntimeException("Invalid identifier or password");
            }
        } else {
            // For OAuth, we'd verify the token with the provider, for now we assume it's valid if we found it
            // This is where OAuth logic would go in the future
        }

        return tokenProvider.generateToken(user);
    }
}
