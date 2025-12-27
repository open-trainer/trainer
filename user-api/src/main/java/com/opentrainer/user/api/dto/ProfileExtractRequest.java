package com.opentrainer.user.api.dto;

import com.opentrainer.domain.common.ProfileSource;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ProfileExtractRequest(
    String username,
    @Enumerated(EnumType.STRING)
    ProfileSource source
) {
}
