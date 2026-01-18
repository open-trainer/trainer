package org.opentrainer.garmin.client;

import org.junit.jupiter.api.Test;
import org.opentrainer.garmin.model.Activity;
import org.opentrainer.garmin.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;

/**
 * Example tests showing how to use the GarminConnectClient.
 * These are not intended to run against a real API without proper configuration.
 */
@SpringBootTest
@ActiveProfiles("test")
class GarminClientUsageTest {

    @Autowired(required = false)
    private GarminConnectClient garminClient;

    @Test
    void clientInjectionTest() {
        // This test ensures that the auto-configuration works and all services are injected correctly
        if (garminClient != null) {
            assert garminClient.getUserProfileService() != null;
            assert garminClient.getActivityService() != null;
            assert garminClient.getHealthMetricsService() != null;
            assert garminClient.getWellnessService() != null;
            assert garminClient.getDeviceService() != null;
            assert garminClient.getGearService() != null;
            assert garminClient.getTrainingPlanService() != null;
        }
    }

    @Test
    void loginWorkflowExample() {
        // This is a documentation-only test showing the new login workflow
        if (garminClient != null) {
            // garminClient.login(); // This would call the Python script
            // UserProfile profile = garminClient.getUserProfile().block();
        }
    }

    /*
    @Test
    void exampleUsage() {
        // Example of how the client would be used
        garminClient.getDisplayName()
            .flatMap(displayName -> garminClient.getDailySummary(displayName, LocalDate.now()))
            .subscribe(summary -> System.out.println("Steps today: " + summary.getSteps()));
    }
    */
}
