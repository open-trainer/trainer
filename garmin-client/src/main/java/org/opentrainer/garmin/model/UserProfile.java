package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * User profile information
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private String displayName;
    private String fullName;
    private String profileImageUrlMedium;
    private String profileImageUrlSmall;
    private String location;
    private String emailAddress;
    private String preferredLocale;
    private MeasurementSystem measurementSystem;
    private Long userProfileId;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MeasurementSystem {
        private String heightUnit;
        private String weightUnit;
        private String distanceUnit;
        private String temperatureUnit;
    }
}
