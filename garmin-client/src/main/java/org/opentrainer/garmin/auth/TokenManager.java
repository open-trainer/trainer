package org.opentrainer.garmin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.config.GarminProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe token manager for storing and retrieving OAuth tokens.
 */
@Slf4j
public class TokenManager {

    private final GarminProperties.OAuth oauthConfig;
    private final ObjectMapper objectMapper;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    private volatile OAuth1Token oauth1Token;
    private volatile OAuth2Token oauth2Token;

    public TokenManager(GarminProperties.OAuth oauthConfig) {
        this.oauthConfig = oauthConfig;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        loadTokensFromDisk();
    }

    /**
     * Get OAuth1 token
     */
    public Optional<OAuth1Token> getOAuth1Token() {
        lock.readLock().lock();
        try {
            if (oauth1Token != null && !oauth1Token.isExpired()) {
                return Optional.of(oauth1Token);
            }
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Get OAuth2 token
     */
    public Optional<OAuth2Token> getOAuth2Token() {
        lock.readLock().lock();
        try {
            if (oauth2Token != null && !oauth2Token.isExpired()) {
                return Optional.of(oauth2Token);
            }
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Store OAuth1 token
     */
    public void storeOAuth1Token(OAuth1Token token) {
        lock.writeLock().lock();
        try {
            this.oauth1Token = token;
            saveTokensToDisk();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Store OAuth2 token
     */
    public void storeOAuth2Token(OAuth2Token token) {
        lock.writeLock().lock();
        try {
            this.oauth2Token = token;
            saveTokensToDisk();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Check if OAuth2 token needs refresh
     */
    public boolean needsRefresh() {
        lock.readLock().lock();
        try {
            if (oauth2Token == null) {
                return false;
            }
            long thresholdSeconds = oauthConfig.getRefreshThreshold().getSeconds();
            return oauth2Token.isExpiringSoon(thresholdSeconds);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Clear all tokens
     */
    public void clearTokens() {
        lock.writeLock().lock();
        try {
            this.oauth1Token = null;
            this.oauth2Token = null;
            deleteTokensFromDisk();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Load tokens from disk
     */
    private void loadTokensFromDisk() {
        try {
            Path tokenPath = getTokenPath();
            if (Files.exists(tokenPath)) {
                TokenStore store = objectMapper.readValue(tokenPath.toFile(), TokenStore.class);
                this.oauth1Token = store.getOauth1Token();
                this.oauth2Token = store.getOauth2Token();
                log.info("Loaded tokens from {}", tokenPath);
            }
        } catch (IOException e) {
            log.warn("Failed to load tokens from disk: {}", e.getMessage());
        }
    }

    /**
     * Save tokens to disk
     */
    private void saveTokensToDisk() {
        try {
            Path tokenPath = getTokenPath();
            Files.createDirectories(tokenPath.getParent());
            
            TokenStore store = TokenStore.builder()
                    .oauth1Token(oauth1Token)
                    .oauth2Token(oauth2Token)
                    .build();
            
            objectMapper.writeValue(tokenPath.toFile(), store);
            log.debug("Saved tokens to {}", tokenPath);
        } catch (IOException e) {
            log.error("Failed to save tokens to disk: {}", e.getMessage());
        }
    }

    /**
     * Delete tokens from disk
     */
    private void deleteTokensFromDisk() {
        try {
            Path tokenPath = getTokenPath();
            Files.deleteIfExists(tokenPath);
            log.info("Deleted tokens from {}", tokenPath);
        } catch (IOException e) {
            log.error("Failed to delete tokens from disk: {}", e.getMessage());
        }
    }

    private Path getTokenPath() {
        return Paths.get(oauthConfig.getTokenStoragePath(), "garmin-tokens.json");
    }

    /**
     * Internal storage class for serialization
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class TokenStore {
        private OAuth1Token oauth1Token;
        private OAuth2Token oauth2Token;
    }
}
