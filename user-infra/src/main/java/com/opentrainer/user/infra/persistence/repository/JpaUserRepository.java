package com.opentrainer.user.infra.persistence.repository;

import com.opentrainer.domain.user.RegistrationOption;
import com.opentrainer.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.registrationOptions ro " +
           "WHERE ro.registrationType = :type AND ro.identifier = :identifier")
    Optional<UserEntity> findByRegistrationOption(@Param("type") RegistrationOption.RegistrationType type,
                                                  @Param("identifier") String identifier);
}
