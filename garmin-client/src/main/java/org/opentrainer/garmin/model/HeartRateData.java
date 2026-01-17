package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Heart rate data for a day
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeartRateData {
    private LocalDate calendarDate;
    private Integer restingHeartRate;
    private Integer maxHeartRate;
    private Integer minHeartRate;
    private List<HeartRateValue> heartRateValues;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeartRateValue {
        private Long timestamp;
        private Integer heartRate;
    }
}
