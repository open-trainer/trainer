package com.opentrainer.user.infra.persistence.adapter;

import com.opentrainer.domain.user.RegistrationOption;
import com.opentrainer.domain.user.User;
import com.opentrainer.domain.user.UserRepository;
import com.opentrainer.user.infra.persistence.entity.RegistrationOptionEntity;
import com.opentrainer.user.infra.persistence.entity.UserEntity;
import com.opentrainer.user.infra.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByRegistrationOption(RegistrationOption.RegistrationType type, String identifier) {
        return jpaUserRepository.findByRegistrationOption(type, identifier).map(this::toDomain);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .active(user.isActive())
                .build();

        if (user.getRegistrationOptions() != null) {
            entity.setRegistrationOptions(user.getRegistrationOptions().stream()
                    .map(option -> RegistrationOptionEntity.builder()
                            .registrationType(option.getType())
                            .identifier(option.getIdentifier())
                            .secret(option.getSecret())
                            .createdAt(option.getCreatedAt())
                            .user(entity)
                            .build())
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .active(entity.isActive())
                .registrationOptions(entity.getRegistrationOptions().stream()
                        .map(optEntity -> RegistrationOption.builder()
                                .type(optEntity.getRegistrationType())
                                .identifier(optEntity.getIdentifier())
                                .secret(optEntity.getSecret())
                                .createdAt(optEntity.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
