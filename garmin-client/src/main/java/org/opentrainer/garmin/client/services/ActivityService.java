package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.model.Activity;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service for activity operations
 */
@Slf4j
@RequiredArgsConstructor
public class ActivityService {

    private final GarminWebClient webClient;

    /**
     * Get activities with pagination
     */
    public Mono<List<Activity>> getActivities(int start, int limit) {
        String path = String.format("/activitylist-service/activities/search/activities?start=%d&limit=%d", 
                start, limit);
        return webClient.get(path, new ParameterizedTypeReference<List<Activity>>() {});
    }

    /**
     * Get activity by ID
     */
    public Mono<Activity> getActivity(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId, Activity.class);
    }

    /**
     * Get activity details with extended data
     */
    public Mono<Map<String, Object>> getActivityDetails(Long activityId, int maxChartSize, int maxPolylineSize) {
        String path = String.format("/activity-service/activity/%d/details?maxChartSize=%d&maxPolylineSize=%d",
                activityId, maxChartSize, maxPolylineSize);
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get activity splits
     */
    public Mono<Map<String, Object>> getActivitySplits(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId + "/splits", 
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get activity weather
     */
    public Mono<Map<String, Object>> getActivityWeather(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId + "/weather",
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Update activity name
     */
    public Mono<Activity> updateActivityName(Long activityId, String newName) {
        Map<String, Object> body = Map.of(
                "activityId", activityId,
                "activityName", newName
        );
        return webClient.put("/activity-service/activity/" + activityId, body, Activity.class);
    }

    /**
     * Update activity type
     */
    public Mono<Activity> updateActivityType(Long activityId, Long typeId, String typeKey, Long parentTypeId) {
        Map<String, Object> body = Map.of(
                "activityId", activityId,
                "activityTypeDTO", Map.of(
                        "typeId", typeId,
                        "typeKey", typeKey,
                        "parentTypeId", parentTypeId
                )
        );
        return webClient.put("/activity-service/activity/" + activityId, body, Activity.class);
    }

    /**
     * Create manual activity from JSON payload
     */
    public Mono<Activity> createManualActivity(Map<String, Object> payload) {
        return webClient.post("/activity-service/activity", payload, Activity.class);
    }

    /**
     * Get available activity types
     */
    public Mono<List<Map<String, Object>>> getActivityTypes() {
        return webClient.get("/activity-service/activity/activityTypes",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get activity HR in time zones
     */
    public Mono<List<Map<String, Object>>> getActivityHrInTimeZones(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId + "/hrTimeInZones",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get activity power in time zones
     */
    public Mono<List<Map<String, Object>>> getActivityPowerInTimeZones(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId + "/powerTimeInZones",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get exercise sets for an activity
     */
    public Mono<List<Map<String, Object>>> getExerciseSets(Long activityId) {
        return webClient.get("/activity-service/activity/" + activityId + "/exerciseSets",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Delete activity
     */
    public Mono<Void> deleteActivity(Long activityId) {
        return webClient.delete("/activity-service/activity/" + activityId);
    }

    /**
     * Get activities count
     */
    public Mono<Long> getActivitiesCount() {
        return webClient.get("/activitylist-service/activities/count", Long.class);
    }

    /**
     * Get last activity
     */
    public Mono<Activity> getLastActivity() {
        return getActivities(0, 1)
                .map(activities -> activities.isEmpty() ? null : activities.get(0));
    }

    /**
     * Get activities for a specific date
     */
    public Mono<List<Activity>> getActivitiesForDate(LocalDate date) {
        String formattedDate = date.toString();
        return webClient.get("/mobile-gateway/heartRate/forDate/" + formattedDate,
                new ParameterizedTypeReference<List<Activity>>() {});
    }
}
