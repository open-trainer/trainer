package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sleep data
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SleepData {
    private Long id;
    private LocalDateTime sleepStartTimestampGMT;
    private LocalDateTime sleepEndTimestampGMT;
    private Integer sleepTimeSeconds;
    private Integer deepSleepSeconds;
    private Integer lightSleepSeconds;
    private Integer remSleepSeconds;
    private Integer awakeSleepSeconds;
    private Double sleepScores;
    private Integer averageRespiration;
    private Integer lowestRespiration;
    private Integer highestRespiration;
    private Double averageSpO2Value;
    private Double lowestSpO2Value;
    private List<SleepLevel> sleepLevels;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SleepLevel {
        private LocalDateTime startGMT;
        private LocalDateTime endGMT;
        private String activityLevel;
    }
}
