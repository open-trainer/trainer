package org.opentrainer.garmin.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * OAuth1 token representation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth1Token implements Serializable {
    private String token;
    private String tokenSecret;
    private Instant expiresAt;

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public boolean isExpiringSoon(long thresholdSeconds) {
        return expiresAt != null && Instant.now().plusSeconds(thresholdSeconds).isAfter(expiresAt);
    }
}
