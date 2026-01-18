package com.opentrainer.user.infra.persistence.entity;

import com.opentrainer.domain.user.RegistrationOption;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_registration_options", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"registration_type", "identifier"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type", nullable = false)
    private RegistrationOption.RegistrationType registrationType;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String secret;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
