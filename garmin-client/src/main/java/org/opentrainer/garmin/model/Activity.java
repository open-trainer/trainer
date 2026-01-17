package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Activity summary
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    private Long activityId;
    private String activityName;
    private String activityType;
    private LocalDateTime startTimeLocal;
    private LocalDateTime startTimeGMT;
    private Double distance;
    private Long duration;
    private Long elapsedDuration;
    private Long movingDuration;
    private Integer calories;
    private Double averageSpeed;
    private Double maxSpeed;
    private Double averageHeartRate;
    private Double maxHeartRate;
    private Integer averagePower;
    private Integer maxPower;
    private Double elevationGain;
    private Double elevationLoss;
    private Integer steps;
    private Double vo2Max;
    private Map<String, Object> summaryDTO;
}
