package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Service for training plans and workouts
 */
@Slf4j
@RequiredArgsConstructor
public class TrainingPlanService {

    private final GarminWebClient webClient;

    /**
     * Get workouts with pagination
     */
    public Mono<List<Map<String, Object>>> getWorkouts(int start, int limit) {
        String path = String.format("/workout-service/workouts?start=%d&limit=%d", start, limit);
        return webClient.get(path, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get workout by ID
     */
    public Mono<Map<String, Object>> getWorkoutById(Long workoutId) {
        return webClient.get("/workout-service/workout/" + workoutId,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get training plans
     */
    public Mono<List<Map<String, Object>>> getTrainingPlans() {
        return webClient.get("/trainingplan-service/trainingplan/plans",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get training plan by ID
     */
    public Mono<Map<String, Object>> getTrainingPlanById(Long planId) {
        return webClient.get("/trainingplan-service/trainingplan/phased/" + planId,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get adaptive training plan by ID
     */
    public Mono<Map<String, Object>> getAdaptiveTrainingPlanById(Long planId) {
        return webClient.get("/trainingplan-service/trainingplan/fbt-adaptive/" + planId,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Upload workout
     */
    public Mono<Map<String, Object>> uploadWorkout(Map<String, Object> payload) {
        return webClient.post("/workout-service/workout", payload,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get scheduled workout
     */
    public Mono<Map<String, Object>> getScheduledWorkoutById(Long scheduledWorkoutId) {
        return webClient.get("/workout-service/schedule/" + scheduledWorkoutId,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
