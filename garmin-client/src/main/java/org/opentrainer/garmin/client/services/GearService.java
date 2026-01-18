package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Service for gear and equipment operations
 */
@Slf4j
@RequiredArgsConstructor
public class GearService {

    private final GarminWebClient webClient;

    /**
     * Get user gear
     * @param userProfilePk The user's profile primary key
     */
    public Mono<List<Map<String, Object>>> getGear(Long userProfilePk) {
        String path = "/gear-service/gear/filterGear?userProfilePk=" + userProfilePk;
        return webClient.get(path, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get gear statistics
     */
    public Mono<Map<String, Object>> getGearStats(String gearUUID) {
        return webClient.get("/gear-service/gear/stats/" + gearUUID, 
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get gear defaults for activity types
     */
    public Mono<List<Map<String, Object>>> getGearDefaults(Long userProfilePk) {
        return webClient.get("/gear-service/gear/user/" + userProfilePk + "/activityTypes",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Set gear default for an activity type
     */
    public Mono<Void> setGearDefault(String gearUUID, String activityType) {
        return webClient.put("/gear-service/gear/" + gearUUID + "/", Map.of("activityType", activityType), Void.class);
    }

    /**
     * Get gear for a specific activity
     */
    public Mono<List<Map<String, Object>>> getActivityGear(Long activityId) {
        return webClient.get("/gear-service/gear/filterGear?activityId=" + activityId,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get activities associated with gear
     */
    public Mono<List<Map<String, Object>>> getGearActivities(String gearUUID, int limit) {
        String path = String.format("/activitylist-service/activities/%s/gear?start=0&limit=%d", gearUUID, limit);
        return webClient.get(path, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Link gear to an activity
     */
    public Mono<Void> addGearToActivity(String gearUUID, Long activityId) {
        return webClient.put("/gear-service/gear/link/" + gearUUID + "/activity/" + activityId, null, Void.class);
    }

    /**
     * Unlink gear from an activity
     */
    public Mono<Void> removeGearFromActivity(String gearUUID, Long activityId) {
        return webClient.put("/gear-service/gear/unlink/" + gearUUID + "/activity/" + activityId, null, Void.class);
    }
}
