package org.opentrainer.garmin.client.http;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.auth.OAuth2Token;
import org.opentrainer.garmin.auth.TokenManager;
import org.opentrainer.garmin.config.GarminProperties;
import org.opentrainer.garmin.exception.GarminAuthenticationException;
import org.opentrainer.garmin.exception.GarminClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Resilient HTTP client for Garmin Connect API with circuit breaker, rate limiting, and retry.
 */
@Slf4j
public class GarminWebClient {

    private final WebClient webClient;
    private final TokenManager tokenManager;
    private final CircuitBreaker circuitBreaker;
    private final RateLimiter rateLimiter;
    private final Retry retry;
    private final GarminProperties properties;

    public GarminWebClient(
            WebClient webClient,
            TokenManager tokenManager,
            CircuitBreaker circuitBreaker,
            RateLimiter rateLimiter,
            Retry retry,
            GarminProperties properties) {
        this.webClient = webClient;
        this.tokenManager = tokenManager;
        this.circuitBreaker = circuitBreaker;
        this.rateLimiter = rateLimiter;
        this.retry = retry;
        this.properties = properties;
    }

    /**
     * Execute GET request
     */
    public <T> Mono<T> get(String path, Class<T> responseType) {
        return executeRequest(
                webClient.get()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute GET request with ParameterizedTypeReference
     */
    public <T> Mono<T> get(String path, ParameterizedTypeReference<T> responseType) {
        return executeRequest(
                webClient.get()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute POST request
     */
    public <T, R> Mono<R> post(String path, T body, Class<R> responseType) {
        return executeRequest(
                webClient.post()
                        .uri(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute POST request without body
     */
    public <R> Mono<R> post(String path, Class<R> responseType) {
        return executeRequest(
                webClient.post()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute POST request with ParameterizedTypeReference
     */
    public <T, R> Mono<R> post(String path, T body, ParameterizedTypeReference<R> responseType) {
        return executeRequest(
                webClient.post()
                        .uri(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute PUT request with ParameterizedTypeReference
     */
    public <T, R> Mono<R> put(String path, T body, ParameterizedTypeReference<R> responseType) {
        return executeRequest(
                webClient.put()
                        .uri(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute PUT request
     */
    public <T, R> Mono<R> put(String path, T body, Class<R> responseType) {
        return executeRequest(
                webClient.put()
                        .uri(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute DELETE request
     */
    public <T> Mono<T> delete(String path, Class<T> responseType) {
        return executeRequest(
                webClient.delete()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(responseType)
        );
    }

    /**
     * Execute DELETE request without response
     */
    public Mono<Void> delete(String path) {
        return executeRequest(
                webClient.delete()
                        .uri(path)
                        .retrieve()
                        .bodyToMono(Void.class)
        );
    }

    /**
     * Execute request with resilience patterns
     */
    private <T> Mono<T> executeRequest(Mono<T> requestMono) {
        return ensureAuthenticated()
                .then(requestMono)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .transformDeferred(RetryOperator.of(retry))
                .onErrorMap(this::mapException)
                .doOnSuccess(response -> {
                    if (properties.getHttp().isLoggingEnabled()) {
                        log.debug("Request successful: {}", response);
                    }
                })
                .doOnError(error -> log.error("Request failed: {}", error.getMessage()));
    }

    /**
     * Ensure OAuth2 token is present and valid
     */
    private Mono<Void> ensureAuthenticated() {
        return Mono.defer(() -> {
            OAuth2Token token = tokenManager.getOAuth2Token()
                    .orElseThrow(() -> new GarminAuthenticationException("No valid OAuth2 token found. Please authenticate first."));

            if (tokenManager.needsRefresh() && properties.getOauth().isAutoRefresh()) {
                log.info("Token needs refresh, initiating refresh flow...");
                // In a real implementation, this would call the refresh token endpoint
                // For now, we throw an exception to indicate re-authentication is needed
                return Mono.error(new GarminAuthenticationException("Token expired, please re-authenticate"));
            }

            return Mono.empty();
        });
    }

    /**
     * Map exceptions to domain-specific exceptions
     */
    private Throwable mapException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException webEx) {
            HttpStatusCode status = webEx.getStatusCode();
            String body = webEx.getResponseBodyAsString();

            if (status.is4xxClientError()) {
                if (status.value() == 401 || status.value() == 403) {
                    return new GarminAuthenticationException("Authentication failed: " + body, throwable);
                }
                return new GarminClientException("Client error: " + body, throwable);
            } else if (status.is5xxServerError()) {
                return new GarminClientException("Server error: " + body, throwable);
            }
        }

        if (throwable instanceof GarminClientException) {
            return throwable;
        }

        return new GarminClientException("Unexpected error: " + throwable.getMessage(), throwable);
    }

    /**
     * Get WebClient for custom operations
     */
    public WebClient getWebClient() {
        return webClient;
    }
}
