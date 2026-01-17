package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

/**
 * Daily activity summary
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailySummary {
    private LocalDate calendarDate;
    private Integer totalSteps;
    private Double totalDistanceMeters;
    private Integer activeTimeInSeconds;
    private Integer floorsAscended;
    private Integer floorsDescended;
    private Integer totalKilocalories;
    private Integer bmrKilocalories;
    private Integer activeKilocalories;
    private Integer moderateIntensityMinutes;
    private Integer vigorousIntensityMinutes;
    private Integer intensityMinutesGoal;
    private Double restingHeartRate;
    private Double maxHeartRate;
}
