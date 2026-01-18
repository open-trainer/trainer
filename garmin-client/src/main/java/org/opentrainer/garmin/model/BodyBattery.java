package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Body battery data
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BodyBattery {
    private LocalDate date;
    private Integer charged;
    private Integer drained;
    private Integer mostRecentValue;
    private List<BodyBatteryValue> values;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BodyBatteryValue {
        private Long timestamp;
        private Integer value;
        private String status;
    }
}
