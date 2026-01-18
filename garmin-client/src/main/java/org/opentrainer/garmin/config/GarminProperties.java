package org.opentrainer.garmin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;

/**
 * Configuration properties for Garmin Connect API client.
 * Allows external configuration through application.yml or application.properties.
 */
@Data
@Validated
@ConfigurationProperties(prefix = "garmin")
public class GarminProperties {

    /**
     * Base URL for Garmin Connect API.
     * Default: https://connect.garmin.com for international
     * Alternative: https://connect.garmin.cn for China
     */
    @NotBlank
    private String baseUrl = "https://connect.garmin.com";

    /**
     * API endpoint prefix
     */
    @NotBlank
    private String apiPath = "/connectapi";

    /**
     * OAuth configuration
     */
    @NotNull
    private OAuth oauth = new OAuth();

    /**
     * HTTP client configuration
     */
    @NotNull
    private Http http = new Http();

    /**
     * Resilience configuration (circuit breaker, rate limiter, retry)
     */
    @NotNull
    private Resilience resilience = new Resilience();

    @Data
    public static class OAuth {
        /**
         * User email for authentication
         */
        private String email;

        /**
         * User password for authentication
         */
        private String password;

        /**
         * Path to the Python executable
         */
        private String pythonPath = "python3";

        /**
         * Path to the java_auth_helper.py script
         */
        private String authHelperPath = "java_auth_helper.py";

        /**
         * OAuth consumer key
         */
        private String consumerKey;

        /**
         * OAuth consumer secret
         */
        private String consumerSecret;

        /**
         * Token storage location
         */
        private String tokenStoragePath = System.getProperty("user.home") + "/.garmin/tokens";

        /**
         * Auto-refresh tokens before expiration
         */
        private boolean autoRefresh = true;

        /**
         * Token refresh threshold (refresh if expires within this duration)
         */
        private Duration refreshThreshold = Duration.ofMinutes(5);
    }

    @Data
    public static class Http {
        /**
         * Connection timeout
         */
        @NotNull
        private Duration connectTimeout = Duration.ofSeconds(10);

        /**
         * Read timeout
         */
        @NotNull
        private Duration readTimeout = Duration.ofSeconds(30);

        /**
         * Write timeout
         */
        @NotNull
        private Duration writeTimeout = Duration.ofSeconds(30);

        /**
         * Maximum connections
         */
        @Positive
        private int maxConnections = 50;

        /**
         * Maximum connections per host
         */
        @Positive
        private int maxConnectionsPerHost = 10;

        /**
         * Enable request/response logging
         */
        private boolean loggingEnabled = false;

        /**
         * User agent string
         */
        @NotBlank
        private String userAgent = "GarminConnectJavaClient/1.0";
    }

    @Data
    public static class Resilience {
        /**
         * Circuit breaker configuration
         */
        @NotNull
        private CircuitBreaker circuitBreaker = new CircuitBreaker();

        /**
         * Rate limiter configuration
         */
        @NotNull
        private RateLimiter rateLimiter = new RateLimiter();

        /**
         * Retry configuration
         */
        @NotNull
        private Retry retry = new Retry();

        @Data
        public static class CircuitBreaker {
            /**
             * Enable circuit breaker
             */
            private boolean enabled = true;

            /**
             * Failure rate threshold (percentage)
             */
            private float failureRateThreshold = 50f;

            /**
             * Slow call rate threshold (percentage)
             */
            private float slowCallRateThreshold = 50f;

            /**
             * Slow call duration threshold
             */
            private Duration slowCallDurationThreshold = Duration.ofSeconds(5);

            /**
             * Wait duration in open state
             */
            private Duration waitDurationInOpenState = Duration.ofSeconds(60);

            /**
             * Sliding window size
             */
            private int slidingWindowSize = 100;

            /**
             * Minimum number of calls
             */
            private int minimumNumberOfCalls = 10;
        }

        @Data
        public static class RateLimiter {
            /**
             * Enable rate limiter
             */
            private boolean enabled = true;

            /**
             * Maximum requests per period
             */
            private int limitForPeriod = 100;

            /**
             * Period duration
             */
            private Duration limitRefreshPeriod = Duration.ofMinutes(1);

            /**
             * Timeout duration for acquiring permission
             */
            private Duration timeoutDuration = Duration.ofSeconds(5);
        }

        @Data
        public static class Retry {
            /**
             * Enable retry
             */
            private boolean enabled = true;

            /**
             * Maximum retry attempts
             */
            private int maxAttempts = 3;

            /**
             * Wait duration between retries
             */
            private Duration waitDuration = Duration.ofMillis(500);

            /**
             * Enable exponential backoff
             */
            private boolean exponentialBackoff = true;

            /**
             * Backoff multiplier
             */
            private double exponentialBackoffMultiplier = 2.0;
        }
    }
}
