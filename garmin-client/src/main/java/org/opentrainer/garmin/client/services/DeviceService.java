package org.opentrainer.garmin.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opentrainer.garmin.client.http.GarminWebClient;
import org.opentrainer.garmin.model.Device;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service for device operations
 */
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final GarminWebClient webClient;

    /**
     * Get all registered devices
     */
    public Mono<List<Device>> getDevices() {
        return webClient.get("/device-service/deviceregistration/devices",
                new ParameterizedTypeReference<List<Device>>() {});
    }

    /**
     * Get device settings
     */
    public Mono<Map<String, Object>> getDeviceSettings(Long deviceId) {
        return webClient.get("/device-service/deviceservice/device-info/settings/" + deviceId,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get primary training device
     */
    public Mono<Device> getPrimaryTrainingDevice() {
        return webClient.get("/web-gateway/device-info/primary-training-device", Device.class);
    }

    /**
     * Get device solar data
     */
    public Mono<Map<String, Object>> getDeviceSolarData(Long deviceId, LocalDate startDate, LocalDate endDate) {
        String path = String.format("/web-gateway/solar/%d/%s/%s",
                deviceId, startDate.toString(), endDate.toString());
        return webClient.get(path, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    /**
     * Get last used device
     */
    public Mono<Device> getDeviceLastUsed() {
        return webClient.get("/device-service/deviceservice/mylastused", Device.class);
    }
}
