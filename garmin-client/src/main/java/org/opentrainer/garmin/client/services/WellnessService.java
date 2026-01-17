package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.model.SleepData;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service for wellness and sleep operations
 */
@Slf4j
@RequiredArgsConstructor
public class WellnessService {

    private final GarminWebClient webClient;

    /**
     * Get lifestyle logging data
     */
    public Mono<Map<String, Object>> getLifestyleLoggingData(LocalDate date) {
        return webClient.get("/lifestylelogging-service/dailyLog/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get menstrual data for date
     */
    public Mono<Map<String, Object>> getMenstrualData(LocalDate date) {
        return webClient.get("/periodichealth-service/menstrualcycle/dayview/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get menstrual calendar data
     */
    public Mono<Map<String, Object>> getMenstrualCalendarData(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/periodichealth-service/menstrualcycle/calendar/%s/%s",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get pregnancy summary
     */
    public Mono<Map<String, Object>> getPregnancySummary() {
        return webClient.get("/periodichealth-service/menstrualcycle/pregnancysnapshot",
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get earned badges
     */
    public Mono<List<Map<String, Object>>> getEarnedBadges() {
        return webClient.get("/badge-service/badge/earned",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get available badges
     */
    public Mono<List<Map<String, Object>>> getAvailableBadges() {
        return webClient.get("/badge-service/badge/available",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get personal records
     */
    public Mono<Map<String, Object>> getPersonalRecords(String displayName) {
        return webClient.get("/personalrecord-service/personalrecord/prs/" + displayName,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get goals
     */
    public Mono<List<Map<String, Object>>> getGoals() {
        return webClient.get("/goal-service/goal/goals",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * Get sleep data for a specific date
     */
    public Mono<SleepData> getSleepData(String displayName, LocalDate date) {
        String path = String.format("/wellness-service/wellness/dailySleepData/%s?date=%s",
                displayName, date.toString());
        return webClient.get(path, SleepData.class);
    }

    /**
     * Get hydration data
     */
    public Mono<Map<String, Object>> getHydrationData(LocalDate date) {
        return webClient.get("/usersummary-service/usersummary/hydration/daily/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Add hydration data
     */
    public Mono<Map<String, Object>> addHydrationData(LocalDate date, int valueInML, int goalInML) {
        Map<String, Object> body = Map.of(
                "calendarDate", date.toString(),
                "valueInML", valueInML,
                "goalInML", goalInML
        );
        return webClient.put("/usersummary-service/usersummary/hydration/log", body,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get blood pressure data for date range
     */
    public Mono<Map<String, Object>> getBloodPressure(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/bloodpressure-service/bloodpressure/range/%s/%s?includeAll=true",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Set blood pressure
     */
    public Mono<Map<String, Object>> setBloodPressure(int systolic, int diastolic, int pulse, String timestamp) {
        Map<String, Object> body = Map.of(
                "systolic", systolic,
                "diastolic", diastolic,
                "pulse", pulse,
                "timestamp", timestamp
        );
        return webClient.post("/bloodpressure-service/bloodpressure", body,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get weigh-ins for date range
     */
    public Mono<Map<String, Object>> getWeighIns(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/weight-service/weight/range/%s/%s?includeAll=true",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get daily weigh-ins
     */
    public Mono<Map<String, Object>> getDailyWeighIns(LocalDate date) {
        String path = String.format("/weight-service/weight/dayview/%s?includeAll=true", date.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Add weigh-in
     */
    public Mono<Map<String, Object>> addWeighIn(double weight, String unit, String timestamp) {
        Map<String, Object> body = Map.of(
                "value", weight,
                "unitKey", unit,
                "dateTimestamp", timestamp,
                "gmtTimestamp", timestamp,
                "sourceType", "MANUAL"
        );
        return webClient.post("/weight-service/user-weight", body,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get all day events
     */
    public Mono<Map<String, Object>> getAllDayEvents(LocalDate date) {
        return webClient.get("/wellness-service/wellness/dailyEvents?calendarDate=" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get resting heart rate for day
     */
    public Mono<Map<String, Object>> getRHRDay(String displayName, LocalDate date) {
        String path = String.format("/userstats-service/wellness/daily/%s?date=%s",
                displayName, date.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
