package org.opentrainer.garmin.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.opentrainer.garmin.auth.TokenManager;
import org.opentrainer.garmin.client.GarminConnectClient;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.client.services.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Auto-configuration for Garmin Connect client.
 */
@Configuration
@EnableConfigurationProperties(GarminProperties.class)
@RequiredArgsConstructor
public class GarminAutoConfiguration {

    private final GarminProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper garminObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenManager tokenManager() {
        return new TokenManager(properties.getOauth());
    }

    @Bean
    @ConditionalOnMissingBean
    public CircuitBreaker garminCircuitBreaker() {
        if (!properties.getResilience().getCircuitBreaker().isEnabled()) {
            return CircuitBreaker.ofDefaults("garmin-dummy");
        }

        var config = properties.getResilience().getCircuitBreaker();
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(config.getFailureRateThreshold())
                .slowCallRateThreshold(config.getSlowCallRateThreshold())
                .slowCallDurationThreshold(config.getSlowCallDurationThreshold())
                .waitDurationInOpenState(config.getWaitDurationInOpenState())
                .slidingWindowSize(config.getSlidingWindowSize())
                .minimumNumberOfCalls(config.getMinimumNumberOfCalls())
                .build();

        return CircuitBreaker.of("garmin", circuitBreakerConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimiter garminRateLimiter() {
        if (!properties.getResilience().getRateLimiter().isEnabled()) {
            return RateLimiter.ofDefaults("garmin-dummy");
        }

        var config = properties.getResilience().getRateLimiter();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(config.getLimitForPeriod())
                .limitRefreshPeriod(config.getLimitRefreshPeriod())
                .timeoutDuration(config.getTimeoutDuration())
                .build();

        return RateLimiter.of("garmin", rateLimiterConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public Retry garminRetry() {
        if (!properties.getResilience().getRetry().isEnabled()) {
            return Retry.ofDefaults("garmin-dummy");
        }

        var config = properties.getResilience().getRetry();
        RetryConfig.Builder<Object> retryConfigBuilder = RetryConfig.custom()
                .maxAttempts(config.getMaxAttempts())
                .waitDuration(config.getWaitDuration());

        if (config.isExponentialBackoff()) {
            retryConfigBuilder.intervalFunction(
                    io.github.resilience4j.core.IntervalFunction.ofExponentialBackoff(
                            config.getWaitDuration(),
                            config.getExponentialBackoffMultiplier()
                    )
            );
        }

        return Retry.of("garmin", retryConfigBuilder.build());
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient garminWebClient(ObjectMapper objectMapper, TokenManager tokenManager) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
                })
                .build();

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl() + properties.getApiPath())
                .defaultHeader("User-Agent", properties.getHttp().getUserAgent())
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GarminWebClient garminHttpClient(
            WebClient garminWebClient,
            TokenManager tokenManager,
            CircuitBreaker circuitBreaker,
            RateLimiter rateLimiter,
            Retry retry,
            ObjectMapper objectMapper) {
        return new GarminWebClient(
                garminWebClient,
                tokenManager,
                circuitBreaker,
                rateLimiter,
                retry,
                objectMapper,
                properties
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public UserProfileService userProfileService(GarminWebClient webClient) {
        return new UserProfileService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ActivityService activityService(GarminWebClient webClient) {
        return new ActivityService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public HealthMetricsService healthMetricsService(GarminWebClient webClient) {
        return new HealthMetricsService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public WellnessService wellnessService(GarminWebClient webClient) {
        return new WellnessService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeviceService deviceService(GarminWebClient webClient) {
        return new DeviceService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public GearService gearService(GarminWebClient webClient) {
        return new GearService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public TrainingPlanService trainingPlanService(GarminWebClient webClient) {
        return new TrainingPlanService(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public GarminConnectClient garminConnectClient(
            UserProfileService userProfileService,
            ActivityService activityService,
            HealthMetricsService healthMetricsService,
            WellnessService wellnessService,
            DeviceService deviceService,
            GearService gearService,
            TrainingPlanService trainingPlanService) {
        return new GarminConnectClient(
                userProfileService,
                activityService,
                healthMetricsService,
                wellnessService,
                deviceService,
                gearService,
                trainingPlanService
        );
    }
}
