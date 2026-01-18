package org.opentrainer.garmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Garmin device information
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    private Long deviceId;
    private String displayName;
    private String productDisplayName;
    private String partNumber;
    private String softwareVersion;
    private String firmwareVersion;
    private LocalDateTime lastSyncTime;
    private Boolean primaryDevice;
    private String deviceType;
}
