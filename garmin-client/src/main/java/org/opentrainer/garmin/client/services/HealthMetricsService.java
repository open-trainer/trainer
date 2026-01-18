package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.model.BodyBattery;
import org.opentrainer.garmin.model.DailySummary;
import org.opentrainer.garmin.model.HeartRateData;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

/**
 * Service for health metrics operations
 */
@Slf4j
@RequiredArgsConstructor
public class HealthMetricsService {

    private final GarminWebClient webClient;

    /**
     * Get daily summary for a specific date
     */
    public Mono<DailySummary> getDailySummary(String displayName, LocalDate date) {
        String path = String.format("/usersummary-service/usersummary/daily/%s?calendarDate=%s",
                displayName, date.toString());
        return webClient.get(path, DailySummary.class);
    }

    /**
     * Get Endurance Score
     */
    public Mono<Map<String, Object>> getEnduranceScore(LocalDate date) {
        String path = "/metrics-service/metrics/endurancescore?calendarDate=" + date.toString();
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Endurance Score Stats
     */
    public Mono<Map<String, Object>> getEnduranceScoreStats(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/metrics-service/metrics/endurancescore/stats?startDate=%s&endDate=%s",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Hill Score
     */
    public Mono<Map<String, Object>> getHillScore(LocalDate date) {
        String path = "/metrics-service/metrics/hillscore?calendarDate=" + date.toString();
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Hill Score Stats
     */
    public Mono<Map<String, Object>> getHillScoreStats(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/metrics-service/metrics/hillscore/stats?startDate=%s&endDate=%s",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Race Predictions
     */
    public Mono<Map<String, Object>> getRacePredictions(String type, String displayName) {
        String path = String.format("/metrics-service/metrics/racepredictions/%s/%s", type, displayName);
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Latest Lactate Threshold
     */
    public Mono<Map<String, Object>> getLatestLactateThreshold() {
        return webClient.get("/biometric-service/biometric/latestLactateThreshold",
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Lactate Threshold for Range
     */
    public Mono<Map<String, Object>> getLactateThresholdRange(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/biometric-service/stats/lactateThreshold/range/%s/%s",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Fitness Age Data
     */
    public Mono<Map<String, Object>> getFitnessAgeData(LocalDate date) {
        return webClient.get("/fitnessage-service/fitnessage/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get Max Metrics
     */
    public Mono<Map<String, Object>> getMaxMetrics(LocalDate date) {
        String path = String.format("/metrics-service/metrics/maxmet/daily/%s/%s",
                date.toString(), date.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get cycling FTP
     */
    public Mono<Map<String, Object>> getCyclingFtp() {
        return webClient.get("/biometric-service/biometric/latestFunctionalThresholdPower/CYCLING",
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get power to weight ratio
     */
    public Mono<Map<String, Object>> getPowerToWeightRatio(LocalDate date) {
        String path = String.format("/biometric-service/biometric/powerToWeight/latest/%s?sport=Running", date.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get steps data for a date
     */
    public Mono<Map<String, Object>> getStepsData(String displayName, LocalDate date) {
        String path = String.format("/wellness-service/wellness/dailySummaryChart/%s?date=%s",
                displayName, date.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get heart rate data for a date
     */
    public Mono<HeartRateData> getHeartRates(String displayName, LocalDate date) {
        String path = String.format("/wellness-service/wellness/dailyHeartRate/%s?date=%s",
                displayName, date.toString());
        return webClient.get(path, HeartRateData.class);
    }

    /**
     * Get body battery data
     */
    public Mono<BodyBattery> getBodyBattery(LocalDate date) {
        String path = String.format("/wellness-service/wellness/bodyBattery/reports/daily?calendarDate=%s",
                date.toString());
        return webClient.get(path, BodyBattery.class);
    }

    /**
     * Get HRV (Heart Rate Variability) data
     */
    public Mono<Map<String, Object>> getHRVData(LocalDate date) {
        return webClient.get("/hrv-service/hrv/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get training readiness
     */
    public Mono<Map<String, Object>> getTrainingReadiness(LocalDate date) {
        return webClient.get("/metrics-service/metrics/trainingreadiness/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get training status
     */
    public Mono<Map<String, Object>> getTrainingStatus(LocalDate date) {
        return webClient.get("/metrics-service/metrics/trainingstatus/aggregated/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get SpO2 data
     */
    public Mono<Map<String, Object>> getSpO2Data(LocalDate date) {
        return webClient.get("/wellness-service/wellness/daily/spo2/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get respiration data
     */
    public Mono<Map<String, Object>> getRespirationData(LocalDate date) {
        return webClient.get("/wellness-service/wellness/daily/respiration/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get stress data
     */
    public Mono<Map<String, Object>> getStressData(LocalDate date) {
        return webClient.get("/wellness-service/wellness/dailyStress/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get intensity minutes
     */
    public Mono<Map<String, Object>> getIntensityMinutes(LocalDate date) {
        return webClient.get("/wellness-service/wellness/daily/im/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get floors climbed
     */
    public Mono<Map<String, Object>> getFloors(LocalDate date) {
        return webClient.get("/wellness-service/wellness/floorsChartData/daily/" + date.toString(),
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get daily steps for date range
     */
    public Mono<Map<String, Object>> getDailySteps(LocalDate startDate, LocalDate endDate) {
        String path = String.format("/usersummary-service/stats/steps/daily/%s/%s",
                startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
