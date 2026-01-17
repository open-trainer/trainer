package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.model.UserProfile;
import reactor.core.publisher.Mono;

/**
 * Service for user profile operations
 */
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final GarminWebClient webClient;

    /**
     * Get user profile information
     */
    public Mono<UserProfile> getUserProfile() {
        return webClient.get("/userprofile-service/userprofile/user-settings", UserProfile.class);
    }

    /**
     * Get user profile settings
     */
    public Mono<UserProfile> getUserProfileSettings() {
        return webClient.get("/userprofile-service/userprofile/settings", UserProfile.class);
    }

    /**
     * Get user profile info (basic)
     */
    public Mono<UserProfile> getUserProfileInfo() {
        return webClient.get("/userprofile-service/userprofile/profile", UserProfile.class);
    }

    /**
     * Get unit system
     */
    public Mono<UserProfile.MeasurementSystem> getUnitSystem() {
        return getUserProfile()
                .map(UserProfile::getMeasurementSystem);
    }

    /**
     * Get user's full name
     */
    public Mono<String> getFullName() {
        return getUserProfileInfo()
                .map(UserProfile::getFullName);
    }

    /**
     * Get user's display name
     */
    public Mono<String> getDisplayName() {
        return getUserProfile()
                .map(UserProfile::getDisplayName);
    }
}
