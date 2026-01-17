package org.opentrainer.garmin.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.services.*;
import org.opentrainer.garmin.model.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Main facade for Garmin Connect API client.
 * Provides access to all service categories.
 * 
 * Usage:
 * <pre>
 * {@code
 * @Autowired
 * private GarminConnectClient garminClient;
 * 
 * // Get user profile
 * UserProfile profile = garminClient.getUserProfile().block();
 * 
 * // Get activities
 * List<Activity> activities = garminClient.getActivities(0, 20).block();
 * 
 * // Get today's summary
 * DailySummary summary = garminClient.getDailySummary(displayName, LocalDate.now()).block();
 * }
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
public class GarminConnectClient {

    @Getter
    private final UserProfileService userProfileService;
    
    @Getter
    private final ActivityService activityService;
    
    @Getter
    private final HealthMetricsService healthMetricsService;
    
    @Getter
    private final WellnessService wellnessService;
    
    @Getter
    private final DeviceService deviceService;

    @Getter
    private final GearService gearService;

    @Getter
    private final TrainingPlanService trainingPlanService;

    // ==================== User Profile Operations ====================

    /**
     * Get user profile information
     */
    public Mono<UserProfile> getUserProfile() {
        return userProfileService.getUserProfile();
    }

    /**
     * Get user profile settings
     */
    public Mono<UserProfile> getUserProfileSettings() {
        return userProfileService.getUserProfileSettings();
    }

    /**
     * Get unit system
     */
    public Mono<UserProfile.MeasurementSystem> getUnitSystem() {
        return userProfileService.getUnitSystem();
    }

    /**
     * Get user's full name
     */
    public Mono<String> getFullName() {
        return userProfileService.getFullName();
    }

    /**
     * Get user's display name
     */
    public Mono<String> getDisplayName() {
        return userProfileService.getDisplayName();
    }

    // ==================== Activity Operations ====================

    /**
     * Get activities with pagination
     * @param start Starting index
     * @param limit Number of activities to retrieve
     */
    public Mono<List<Activity>> getActivities(int start, int limit) {
        return activityService.getActivities(start, limit);
    }

    /**
     * Get activity by ID
     */
    public Mono<Activity> getActivity(Long activityId) {
        return activityService.getActivity(activityId);
    }

    /**
     * Get detailed activity data
     */
    public Mono<Map<String, Object>> getActivityDetails(Long activityId, int maxChartSize, int maxPolylineSize) {
        return activityService.getActivityDetails(activityId, maxChartSize, maxPolylineSize);
    }

    /**
     * Get activity splits
     */
    public Mono<Map<String, Object>> getActivitySplits(Long activityId) {
        return activityService.getActivitySplits(activityId);
    }

    /**
     * Update activity name
     */
    public Mono<Activity> updateActivityName(Long activityId, String newName) {
        return activityService.updateActivityName(activityId, newName);
    }

    /**
     * Update activity type
     */
    public Mono<Activity> updateActivityType(Long activityId, Long typeId, String typeKey, Long parentTypeId) {
        return activityService.updateActivityType(activityId, typeId, typeKey, parentTypeId);
    }

    /**
     * Create manual activity
     */
    public Mono<Activity> createManualActivity(Map<String, Object> payload) {
        return activityService.createManualActivity(payload);
    }

    /**
     * Get available activity types
     */
    public Mono<List<Map<String, Object>>> getActivityTypes() {
        return activityService.getActivityTypes();
    }

    /**
     * Delete activity
     */
    public Mono<Void> deleteActivity(Long activityId) {
        return activityService.deleteActivity(activityId);
    }

    /**
     * Get total activities count
     */
    public Mono<Long> getActivitiesCount() {
        return activityService.getActivitiesCount();
    }

    /**
     * Get last activity
     */
    public Mono<Activity> getLastActivity() {
        return activityService.getLastActivity();
    }

    /**
     * Get activities for a specific date
     */
    public Mono<List<Activity>> getActivitiesForDate(LocalDate date) {
        return activityService.getActivitiesForDate(date);
    }

    // ==================== Health Metrics Operations ====================

    /**
     * Get daily summary for a specific date
     * @param displayName User's display name (can be retrieved with getDisplayName())
     * @param date Date to retrieve summary for
     */
    public Mono<DailySummary> getDailySummary(String displayName, LocalDate date) {
        return healthMetricsService.getDailySummary(displayName, date);
    }

    /**
     * Get endurance score
     */
    public Mono<Map<String, Object>> getEnduranceScore(LocalDate date) {
        return healthMetricsService.getEnduranceScore(date);
    }

    /**
     * Get hill score
     */
    public Mono<Map<String, Object>> getHillScore(LocalDate date) {
        return healthMetricsService.getHillScore(date);
    }

    /**
     * Get race predictions
     */
    public Mono<Map<String, Object>> getRacePredictions(String type, String displayName) {
        return healthMetricsService.getRacePredictions(type, displayName);
    }

    /**
     * Get steps data for a date
     */
    public Mono<Map<String, Object>> getStepsData(String displayName, LocalDate date) {
        return healthMetricsService.getStepsData(displayName, date);
    }

    /**
     * Get heart rate data for a date
     */
    public Mono<HeartRateData> getHeartRates(String displayName, LocalDate date) {
        return healthMetricsService.getHeartRates(displayName, date);
    }

    /**
     * Get body battery data
     */
    public Mono<BodyBattery> getBodyBattery(LocalDate date) {
        return healthMetricsService.getBodyBattery(date);
    }

    /**
     * Get HRV data
     */
    public Mono<Map<String, Object>> getHRVData(LocalDate date) {
        return healthMetricsService.getHRVData(date);
    }

    /**
     * Get training readiness
     */
    public Mono<Map<String, Object>> getTrainingReadiness(LocalDate date) {
        return healthMetricsService.getTrainingReadiness(date);
    }

    /**
     * Get training status
     */
    public Mono<Map<String, Object>> getTrainingStatus(LocalDate date) {
        return healthMetricsService.getTrainingStatus(date);
    }

    /**
     * Get SpO2 data
     */
    public Mono<Map<String, Object>> getSpO2Data(LocalDate date) {
        return healthMetricsService.getSpO2Data(date);
    }

    /**
     * Get stress data
     */
    public Mono<Map<String, Object>> getStressData(LocalDate date) {
        return healthMetricsService.getStressData(date);
    }

    /**
     * Get daily steps for date range
     */
    public Mono<Map<String, Object>> getDailySteps(LocalDate startDate, LocalDate endDate) {
        return healthMetricsService.getDailySteps(startDate, endDate);
    }

    // ==================== Wellness Operations ====================

    /**
     * Get sleep data for a specific date
     */
    public Mono<SleepData> getSleepData(String displayName, LocalDate date) {
        return wellnessService.getSleepData(displayName, date);
    }

    /**
     * Get hydration data
     */
    public Mono<Map<String, Object>> getHydrationData(LocalDate date) {
        return wellnessService.getHydrationData(date);
    }

    /**
     * Add hydration data
     */
    public Mono<Map<String, Object>> addHydrationData(LocalDate date, int valueInML, int goalInML) {
        return wellnessService.addHydrationData(date, valueInML, goalInML);
    }

    /**
     * Get blood pressure data
     */
    public Mono<Map<String, Object>> getBloodPressure(LocalDate startDate, LocalDate endDate) {
        return wellnessService.getBloodPressure(startDate, endDate);
    }

    /**
     * Set blood pressure
     */
    public Mono<Map<String, Object>> setBloodPressure(int systolic, int diastolic, int pulse, String timestamp) {
        return wellnessService.setBloodPressure(systolic, diastolic, pulse, timestamp);
    }

    /**
     * Get weigh-ins for date range
     */
    public Mono<Map<String, Object>> getWeighIns(LocalDate startDate, LocalDate endDate) {
        return wellnessService.getWeighIns(startDate, endDate);
    }

    /**
     * Add weigh-in
     */
    public Mono<Map<String, Object>> addWeighIn(double weight, String unit, String timestamp) {
        return wellnessService.addWeighIn(weight, unit, timestamp);
    }

    /**
     * Get earned badges
     */
    public Mono<List<Map<String, Object>>> getEarnedBadges() {
        return wellnessService.getEarnedBadges();
    }

    /**
     * Get personal records
     */
    public Mono<Map<String, Object>> getPersonalRecords(String displayName) {
        return wellnessService.getPersonalRecords(displayName);
    }

    /**
     * Get goals
     */
    public Mono<List<Map<String, Object>>> getGoals() {
        return wellnessService.getGoals();
    }

    // ==================== Device Operations ====================

    /**
     * Get all registered devices
     */
    public Mono<List<Device>> getDevices() {
        return deviceService.getDevices();
    }

    /**
     * Get device settings
     */
    public Mono<Map<String, Object>> getDeviceSettings(Long deviceId) {
        return deviceService.getDeviceSettings(deviceId);
    }

    /**
     * Get primary training device
     */
    public Mono<Device> getPrimaryTrainingDevice() {
        return deviceService.getPrimaryTrainingDevice();
    }

    /**
     * Get last used device
     */
    public Mono<Device> getDeviceLastUsed() {
        return deviceService.getDeviceLastUsed();
    }

    // ==================== Gear Operations ====================

    /**
     * Get user gear
     */
    public Mono<List<Map<String, Object>>> getGear(Long userProfilePk) {
        return gearService.getGear(userProfilePk);
    }

    /**
     * Link gear to activity
     */
    public Mono<Void> addGearToActivity(String gearUUID, Long activityId) {
        return gearService.addGearToActivity(gearUUID, activityId);
    }

    // ==================== Training Plan Operations ====================

    /**
     * Get workouts
     */
    public Mono<List<Map<String, Object>>> getWorkouts(int start, int limit) {
        return trainingPlanService.getWorkouts(start, limit);
    }

    /**
     * Get training plans
     */
    public Mono<List<Map<String, Object>>> getTrainingPlans() {
        return trainingPlanService.getTrainingPlans();
    }
}
