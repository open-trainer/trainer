package org.opentrainer.garmin.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.config.GarminProperties;
import org.opentrainer.garmin.exception.GarminAuthenticationException;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Service for authenticating with Garmin using a Python helper script.
 */
@Slf4j
@RequiredArgsConstructor
public class PythonAuthService {

    private final GarminProperties properties;
    private final TokenManager tokenManager;
    private final JsonMapper objectMapper;

    /**
     * Authenticate using the Python helper script and store tokens.
     */
    public void authenticate() {
        GarminProperties.OAuth oauth = properties.getOauth();
        String email = oauth.getEmail();
        String password = oauth.getPassword();

        if (email == null || password == null) {
            throw new GarminAuthenticationException("Email and password must be configured for Python authentication.");
        }

        try {
            log.info("Starting Python authentication helper for user: {}", email);
            ProcessBuilder pb = new ProcessBuilder(
                    oauth.getPythonPath(),
                    oauth.getAuthHelperPath(),
                    "authenticate",
                    email,
                    password
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Python authentication failed with exit code {}. Output: {}", exitCode, output);
                throw new GarminAuthenticationException("Python authentication failed. See logs for details.");
            }

            // Find JSON in output (in case there's some debug logging before it)
            int jsonStart = output.indexOf("{");
            if (jsonStart == -1) {
                throw new GarminAuthenticationException("No JSON found in Python script output: " + output);
            }
            String jsonOutput = output.substring(jsonStart);

            PythonTokens pythonTokens = objectMapper.readValue(jsonOutput, PythonTokens.class);
            mapAndStoreTokens(pythonTokens);
            log.info("Successfully authenticated and stored tokens.");

        } catch (Exception e) {
            log.error("Error during Python authentication", e);
            throw new GarminAuthenticationException("Failed to execute Python authentication: " + e.getMessage());
        }
    }

    private void mapAndStoreTokens(PythonTokens tokens) {
        if (tokens.oauth1Token != null) {
            OAuth1Token oauth1 = OAuth1Token.builder()
                    .token(tokens.oauth1Token.token)
                    .tokenSecret(tokens.oauth1Token.tokenSecret)
                    .build();
            tokenManager.storeOAuth1Token(oauth1);
        }

        if (tokens.oauth2Token != null) {
            OAuth2Token oauth2 = OAuth2Token.builder()
                    .accessToken(tokens.oauth2Token.accessToken)
                    .refreshToken(tokens.oauth2Token.refreshToken)
                    .tokenType(tokens.oauth2Token.tokenType)
                    .scope(tokens.oauth2Token.scope)
                    .expiresAt(tokens.oauth2Token.expiresIn != null ? 
                            Instant.now().plusSeconds(tokens.oauth2Token.expiresIn) : null)
                    .build();
            tokenManager.storeOAuth2Token(oauth2);
        }
    }

    /**
     * DTO for parsing Python script output
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    private static class PythonTokens {
        @JsonProperty("oauth1")
        private PythonOAuth1Token oauth1Token;
        @JsonProperty("oauth2")
        private PythonOAuth2Token oauth2Token;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    private static class PythonOAuth1Token {
        @JsonProperty("token")
        private String token;
        @JsonProperty("token_secret")
        private String tokenSecret;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    private static class PythonOAuth2Token {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private Long expiresIn;
        @JsonProperty("scope")
        private String scope;
    }
}
