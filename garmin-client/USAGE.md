# Garmin Connect Client - Usage Guide

This is a Java client for Garmin Connect API that allows you to authenticate and fetch data from your Garmin account.

## Authentication

### Java-based Login/Password Authentication (Recommended)

The simplest way to authenticate is using your email and password:

```java
@Autowired
private GarminConnectClient client;

// Authenticate with email and password
client.authenticate("your.email@example.com", "password")
    .block(); // or subscribe() for reactive flow
```

### Using Configuration Properties

Configure your credentials in `application.yaml`:

```yaml
garmin:
  base-url: https://connect.garmin.com
  oauth:
    email: your.email@example.com
    password: your-password
    token-storage-path: ${user.home}/.garmin/tokens
    auto-refresh: true
```

Then authenticate:

```java
@Autowired
private GarminConnectClient client;

// Authenticate using configured credentials
client.authenticate(
    garminProperties.getOauth().getEmail(),
    garminProperties.getOauth().getPassword()
).block();
```

### Cached Token Authentication

Tokens are automatically cached after successful authentication. On subsequent runs, if the token is still valid, you don't need to re-authenticate:

```java
@Autowired
private TokenManager tokenManager;

// Check if we have a valid token
if (tokenManager.getOAuth2Token().isEmpty()) {
    // Authenticate if no valid token exists
    client.authenticate(email, password).block();
}

// Now you can use the client
var activities = client.getActivities(0, 10).block();
```

## Features

### Activities

#### Fetch list of activities

```java
// Get 10 most recent activities
List<Activity> activities = client.getActivities(0, 10).block();

// Get activities for a specific date
List<Activity> todayActivities = client.getActivitiesForDate(LocalDate.now()).block();

// Get single activity by ID
Activity activity = client.getActivity(123456789L).block();
```

#### Get activity details

```java
// Get detailed activity data
Map<String, Object> details = client.getActivityDetails(
    activityId,
    1000,  // max chart size
    4000   // max polyline size
).block();

// Get activity splits
Map<String, Object> splits = client.getActivitySplits(activityId).block();
```

#### Manage activities

```java
// Update activity name
Activity updated = client.updateActivityName(activityId, "Morning Run").block();

// Delete activity
client.deleteActivity(activityId).block();
```

### User Profile

```java
// Get user profile
UserProfile profile = client.getUserProfile().block();

// Get display name
String displayName = client.getDisplayName().block();

// Get full name
String fullName = client.getFullName().block();
```

### Health Metrics

```java
String displayName = client.getDisplayName().block();
LocalDate today = LocalDate.now();

// Get daily summary
DailySummary summary = client.getDailySummary(displayName, today).block();

// Get heart rate data
HeartRateData heartRate = client.getHeartRates(displayName, today).block();

// Get body battery
BodyBattery bodyBattery = client.getBodyBattery(today).block();

// Get steps data
Map<String, Object> steps = client.getStepsData(displayName, today).block();

// Get stress data
Map<String, Object> stress = client.getStressData(today).block();
```

### Sleep Data

```java
String displayName = client.getDisplayName().block();
LocalDate today = LocalDate.now();

// Get sleep data for a specific date
SleepData sleep = client.getSleepData(displayName, today).block();
```

### Devices

```java
// Get all registered devices
List<Device> devices = client.getDevices().block();

// Get primary training device
Device primary = client.getPrimaryTrainingDevice().block();

// Get last used device
Device lastUsed = client.getDeviceLastUsed().block();
```

### Wellness

```java
// Get hydration data
Map<String, Object> hydration = client.getHydrationData(LocalDate.now()).block();

// Add hydration
client.addHydrationData(LocalDate.now(), 500, 2000).block();

// Get weigh-ins
Map<String, Object> weighIns = client.getWeighIns(
    LocalDate.now().minusDays(7),
    LocalDate.now()
).block();

// Add weigh-in
client.addWeighIn(75.5, "kg", Instant.now().toString()).block();
```

## Exception Handling

The client uses custom exceptions for different error scenarios:

```java
try {
    client.authenticate(email, password).block();
} catch (GarminAuthenticationException e) {
    // Invalid credentials or authentication failed
    log.error("Authentication failed: {}", e.getMessage());
} catch (GarminClientException e) {
    // Other client errors
    log.error("Client error: {}", e.getMessage());
}
```

## Reactive Programming

All methods return `Mono<T>` from Project Reactor for non-blocking operations:

```java
// Reactive approach
client.authenticate(email, password)
    .then(client.getActivities(0, 10))
    .subscribe(
        activities -> log.info("Found {} activities", activities.size()),
        error -> log.error("Error: {}", error.getMessage())
    );

// Blocking approach (for testing or simple use cases)
List<Activity> activities = client.getActivities(0, 10).block();
```

## Configuration Options

Full configuration example in `application.yaml`:

```yaml
garmin:
  base-url: https://connect.garmin.com
  api-path: /connectapi

  oauth:
    email: your.email@example.com
    password: your-password
    token-storage-path: ${user.home}/.garmin/tokens
    auto-refresh: true
    refresh-threshold: 5m

  http:
    connect-timeout: 10s
    read-timeout: 30s
    write-timeout: 30s
    max-connections: 50
    max-connections-per-host: 10
    logging-enabled: false
    user-agent: GarminConnectJavaClient/1.0

  resilience:
    circuit-breaker:
      enabled: true
      failure-rate-threshold: 50
      slow-call-rate-threshold: 50
      slow-call-duration-threshold: 5s
      wait-duration-in-open-state: 60s
      sliding-window-size: 100
      minimum-number-of-calls: 10

    rate-limiter:
      enabled: true
      limit-for-period: 100
      limit-refresh-period: 1m
      timeout-duration: 5s

    retry:
      enabled: true
      max-attempts: 3
      wait-duration: 500ms
      exponential-backoff: true
      exponential-backoff-multiplier: 2.0
```
