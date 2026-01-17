package org.opentrainer.garmin.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * OAuth2 token representation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Token implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Instant expiresAt;
    private String scope;

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public boolean isExpiringSoon(long thresholdSeconds) {
        return expiresAt != null && Instant.now().plusSeconds(thresholdSeconds).isAfter(expiresAt);
    }
}
