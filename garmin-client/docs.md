ROLE:
You are a senior Java engineer with production experience in AI and garmin API.

GOAL:
in garmin client library, implement 
# Garmin Connect API Analysis

This document lists all API endpoints extracted from the python-garminconnect project.

## Base URLs

- **International**: `https://connect.garmin.com`
- **China**: `https://connect.garmin.cn`

All API calls go through the `/connectapi` service endpoint.

## Authentication

The library uses OAuth1/OAuth2 tokens managed by the `garth` library. Tokens are stored locally and refreshed automatically.


CONSTRAINTS:
- Language: Java 17
- Libraries/frameworks allowed: Spring can be used to configure, set http clients
- Performance requirements: Every request should be idenpotent, apply token
- Thread-safety, async, circuit breaker, http limits configuration

INPUT:

## API Endpoints by Category

### User & Profile (4 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_full_name()` | `/userprofile-service/userprofile/profile` | Get user's full name | GET |
| `get_user_profile()` | `/userprofile-service/userprofile/user-settings` | Get all user settings | GET |
| `get_userprofile_settings()` | `/userprofile-service/userprofile/settings` | Get user profile settings | GET |
| `get_unit_system()` | `/userprofile-service/userprofile/user-settings` | Get unit system (metric/imperial) | GET |

---

### Daily Health & Activity (9 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_user_summary(date)` | `/usersummary-service/usersummary/daily/{displayName}?calendarDate={date}` | Get daily activity summary | GET |
| `get_steps_data(date)` | `/wellness-service/wellness/dailySummaryChart/{displayName}?date={date}` | Get hourly steps data | GET |
| `get_floors(date)` | `/wellness-service/wellness/floorsChartData/daily/{date}` | Get floors climbed | GET |
| `get_daily_steps(start, end)` | `/usersummary-service/stats/steps/daily/{start}/{end}` | Get daily steps for date range | GET |
| `get_heart_rates(date)` | `/wellness-service/wellness/dailyHeartRate/{displayName}?date={date}` | Get daily heart rate data | GET |
| `get_respiration_data(date)` | `/wellness-service/wellness/daily/respiration/{date}` | Get respiration data | GET |
| `get_spo2_data(date)` | `/wellness-service/wellness/daily/spo2/{date}` | Get SpO2 (oxygen saturation) data | GET |
| `get_intensity_minutes_data(date)` | `/wellness-service/wellness/daily/im/{date}` | Get intensity minutes | GET |
| `get_all_day_stress(date)` | `/wellness-service/wellness/dailyStress/{date}` | Get all-day stress data | GET |

---

### Advanced Health Metrics (11 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_body_battery(date)` | `/wellness-service/wellness/bodyBattery/reports/daily?calendarDate={date}` | Get body battery data | GET |
| `get_body_battery_events(date)` | `/wellness-service/wellness/bodyBattery/events/{date}` | Get body battery events | GET |
| `get_hrv_data(date)` | `/hrv-service/hrv/{date}` | Get HRV (Heart Rate Variability) data | GET |
| `get_training_readiness(date)` | `/metrics-service/metrics/trainingreadiness/{date}` | Get training readiness | GET |
| `get_morning_training_readiness(date)` | `/metrics-service/metrics/trainingreadiness/{date}` | Get morning training readiness | GET |
| `get_endurance_score(date)` | `/metrics-service/metrics/endurancescore?calendarDate={date}` | Get endurance score | GET |
| `get_endurance_score_stats(start, end)` | `/metrics-service/metrics/endurancescore/stats?startDate={start}&endDate={end}` | Get endurance score stats | GET |
| `get_hill_score(date)` | `/metrics-service/metrics/hillscore?calendarDate={date}` | Get hill score | GET |
| `get_hill_score_stats(start, end)` | `/metrics-service/metrics/hillscore/stats?startDate={start}&endDate={end}` | Get hill score stats | GET |
| `get_race_predictions(type)` | `/metrics-service/metrics/racepredictions/{type}/{displayName}` or `/latest/{displayName}` | Get race predictions | GET |
| `get_training_status(date)` | `/metrics-service/metrics/trainingstatus/aggregated/{date}` | Get training status | GET |

---

### Historical Data & Trends (9 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_weekly_steps(end, weeks)` | `/usersummary-service/stats/steps/weekly/{end}/{weeks}` | Get weekly steps data | GET |
| `get_weekly_stress(end, weeks)` | `/usersummary-service/stats/stress/weekly/{end}/{weeks}` | Get weekly stress data | GET |
| `get_weekly_intensity_minutes(start, end)` | `/usersummary-service/stats/im/weekly/{start}/{end}` | Get weekly intensity minutes | GET |
| `get_user_summary_chart(date)` | `/wellness-service/wellness/dailySummaryChart/{displayName}?date={date}` | Get daily summary chart | GET |
| `get_activities_by_date(start, end, type, sort)` | `/activitylist-service/activities/search/activities` | Get activities by date range | GET |
| `get_progress_summary_between_dates(start, end)` | `/fitnessstats-service/activity?startDate={start}&endDate={end}` | Get progress summary | GET |
| `get_lactate_threshold(latest/range)` | `/biometric-service/biometric/latestLactateThreshold` or `/biometric-service/stats/lactateThreshold*/range/{start}/{end}` | Get lactate threshold data | GET |
| `get_fitnessage_data(date)` | `/fitnessage-service/fitnessage/{date}` | Get fitness age data | GET |
| `get_max_metrics(date)` | `/metrics-service/metrics/maxmet/daily/{date}/{date}` | Get max metrics | GET |

---

### Activities & Workouts (28 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_activities(start, limit)` | `/activitylist-service/activities/search/activities?start={start}&limit={limit}` | Get activities list | GET |
| `get_activities_count()` | `/activitylist-service/activities/count` | Get activities count | GET |
| `get_activities_fordate(date)` | `/mobile-gateway/heartRate/forDate/{date}` | Get activities for date | GET |
| `get_activity(activity_id)` | `/activity-service/activity/{activity_id}` | Get activity summary | GET |
| `get_activity_details(activity_id)` | `/activity-service/activity/{activity_id}/details?maxChartSize={maxchart}&maxPolylineSize={maxpoly}` | Get activity details | GET |
| `get_activity_splits(activity_id)` | `/activity-service/activity/{activity_id}/splits` | Get activity splits | GET |
| `get_activity_typed_splits(activity_id)` | `/activity-service/activity/{activity_id}/typedsplits` | Get typed splits | GET |
| `get_activity_split_summaries(activity_id)` | `/activity-service/activity/{activity_id}/split_summaries` | Get split summaries | GET |
| `get_activity_weather(activity_id)` | `/activity-service/activity/{activity_id}/weather` | Get activity weather | GET |
| `get_activity_hr_in_timezones(activity_id)` | `/activity-service/activity/{activity_id}/hrTimeInZones` | Get HR time in zones | GET |
| `get_activity_power_in_timezones(activity_id)` | `/activity-service/activity/{activity_id}/powerTimeInZones` | Get power time in zones | GET |
| `get_activity_exercise_sets(activity_id)` | `/activity-service/activity/{activity_id}/exerciseSets` | Get exercise sets | GET |
| `get_last_activity()` | Uses `get_activities(0, 1)` | Get last activity | GET |
| `set_activity_name(activity_id, title)` | `/activity-service/activity/{activity_id}` | Update activity name | PUT |
| `set_activity_type(activity_id, type_id, type_key, parent_type_id)` | `/activity-service/activity/{activity_id}` | Update activity type | PUT |
| `create_manual_activity_from_json(payload)` | `/activity-service/activity` | Create manual activity | POST |
| `upload_activity(file_path)` | `/upload-service/upload` | Upload activity file (FIT, GPX, TCX, etc.) | POST |
| `delete_activity(activity_id)` | `/activity-service/activity/{activity_id}` | Delete activity | DELETE |
| `download_activity(activity_id, format)` | `/download-service/files/activity/{activity_id}` or `/export/{format}/activity/{activity_id}` | Download activity in various formats | GET |
| `get_activity_types()` | `/activity-service/activity/activityTypes` | Get available activity types | GET |
| `get_workouts(start, limit)` | `/workout-service/workouts?start={start}&limit={limit}` | Get workouts | GET |
| `get_workout_by_id(workout_id)` | `/workout-service/workout/{workout_id}` | Get workout by ID | GET |
| `download_workout(workout_id)` | `/workout-service/workout/FIT/{workout_id}` | Download workout as FIT | GET |
| `upload_workout(payload)` | `/workout-service/workout` | Upload workout | POST |
| `upload_running_workout(workout)` | `/workout-service/workout` | Upload running workout | POST |
| `upload_cycling_workout(workout)` | `/workout-service/workout` | Upload cycling workout | POST |
| `upload_swimming_workout(workout)` | `/workout-service/workout` | Upload swimming workout | POST |
| `upload_walking_workout(workout)` | `/workout-service/workout` | Upload walking workout | POST |
| `upload_hiking_workout(workout)` | `/workout-service/workout` | Upload hiking workout | POST |
| `get_scheduled_workout_by_id(scheduled_workout_id)` | `/workout-service/schedule/{scheduled_workout_id}` | Get scheduled workout | GET |

---

### Body Composition & Weight (8 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_body_composition(startdate, enddate)` | `/weight-service/weight/dateRange?startDate={startdate}&endDate={enddate}` | Get body composition range | GET |
| `add_body_composition(...)` | `/upload-service/upload` | Upload body composition data (FIT) | POST |
| `add_weigh_in(weight, unit, timestamp)` | `/weight-service/user-weight` | Add weight entry | POST |
| `add_weigh_in_with_timestamps(...)` | `/weight-service/user-weight` | Add weight with explicit timestamps | POST |
| `get_weigh_ins(startdate, enddate)` | `/weight-service/weight/range/{startdate}/{enddate}?includeAll=true` | Get weigh-ins for range | GET |
| `get_daily_weigh_ins(date)` | `/weight-service/weight/dayview/{date}?includeAll=true` | Get daily weigh-ins | GET |
| `delete_weigh_in(weight_pk, date)` | `/weight-service/weight/{date}/byversion/{weight_pk}` | Delete specific weigh-in | DELETE |
| `delete_weigh_ins(date, delete_all)` | `/weight-service/weight/{date}/byversion/{weight_pk}` | Delete weigh-ins | DELETE |

---

### Goals & Achievements (15 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_personal_record()` | `/personalrecord-service/personalrecord/prs/{displayName}` | Get personal records | GET |
| `get_earned_badges()` | `/badge-service/badge/earned` | Get earned badges | GET |
| `get_available_badges()` | `/badge-service/badge/available` | Get available badges | GET |
| `get_in_progress_badges()` | `/badge-service/badge/earned` | Get in-progress badges | GET |
| `get_adhoc_challenges(start, limit)` | `/adhocchallenge-service/adHocChallenge/historical?start={start}&limit={limit}` | Get ad-hoc challenges | GET |
| `get_badge_challenges(start, limit)` | `/badgechallenge-service/badgeChallenge/completed?start={start}&limit={limit}` | Get completed badge challenges | GET |
| `get_available_badge_challenges(start, limit)` | `/badgechallenge-service/badgeChallenge/available?start={start}&limit={limit}` | Get available badge challenges | GET |
| `get_non_completed_badge_challenges(start, limit)` | `/badgechallenge-service/badgeChallenge/non-completed?start={start}&limit={limit}` | Get non-completed badge challenges | GET |
| `get_inprogress_virtual_challenges(start, limit)` | `/badgechallenge-service/virtualChallenge/inProgress?start={start}&limit={limit}` | Get in-progress virtual challenges | GET |
| `get_goals()` | `/goal-service/goal/goals` | Get goals | GET |

---

### Device & Technical (7 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_devices()` | `/device-service/deviceregistration/devices` | Get all devices | GET |
| `get_device_settings(device_id)` | `/device-service/deviceservice/device-info/settings/{device_id}` | Get device settings | GET |
| `get_primary_training_device()` | `/web-gateway/device-info/primary-training-device` | Get primary training device | GET |
| `get_device_solar_data(device_id, startdate, enddate)` | `/web-gateway/solar/{device_id}/{startdate}/{enddate}` | Get device solar data | GET |
| `get_device_alarms()` | `/device-service/deviceservice/mylastused` | Get device alarms | GET |
| `get_device_last_used()` | `/device-service/deviceservice/mylastused` | Get last used device | GET |

---

### Gear & Equipment (8 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_gear(userProfileNumber)` | `/gear-service/gear/filterGear?userProfilePk={userProfileNumber}` | Get user gear | GET |
| `get_gear_stats(gearUUID)` | `/gear-service/gear/stats/{gearUUID}` | Get gear statistics | GET |
| `get_gear_defaults(userProfileNumber)` | `/gear-service/gear/user/{userProfileNumber}/activityTypes` | Get gear defaults | GET |
| `set_gear_default(gearUUID, activityType)` | `/gear-service/gear/{gearUUID}/` | Set gear default | PUT |
| `get_activity_gear(activity_id)` | `/gear-service/gear/filterGear?activityId={activity_id}` | Get gear for activity | GET |
| `get_gear_activities(gearUUID, limit)` | `/activitylist-service/activities/{gearUUID}/gear?start=0&limit={limit}` | Get activities for gear | GET |
| `add_gear_to_activity(gearUUID, activity_id)` | `/gear-service/gear/link/{gearUUID}/activity/{activity_id}` | Link gear to activity | PUT |
| `remove_gear_from_activity(gearUUID, activity_id)` | `/gear-service/gear/unlink/{gearUUID}/activity/{activity_id}` | Unlink gear from activity | PUT |

---

### Hydration & Wellness (9 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_hydration_data(date)` | `/usersummary-service/usersummary/hydration/daily/{date}` | Get daily hydration | GET |
| `add_hydration_data(valueInML, goalInML, date)` | `/usersummary-service/usersummary/hydration/log` | Add hydration data | PUT |
| `set_blood_pressure(...)` | `/bloodpressure-service/bloodpressure` | Set blood pressure | POST |
| `get_blood_pressure(startdate, enddate)` | `/bloodpressure-service/bloodpressure/range/{startdate}/{enddate}?includeAll=true` | Get blood pressure range | GET |
| `delete_blood_pressure(version, date)` | `/bloodpressure-service/bloodpressure/{date}/{version}` | Delete blood pressure entry | DELETE |
| `get_all_day_events(date)` | `/wellness-service/wellness/dailyEvents?calendarDate={date}` | Get all day events | GET |
| `get_sleep_data(date)` | `/wellness-service/wellness/dailySleepData/{displayName}?date={date}` | Get sleep data | GET |
| `get_stress_data(date)` | `/wellness-service/wellness/dailyStress/{date}` | Get stress data | GET |
| `get_lifestyle_logging_data(date)` | `/lifestylelogging-service/dailyLog/{date}` | Get lifestyle logging | GET |
| `get_rhr_day(date)` | `/userstats-service/wellness/daily/{displayName}?date={date}` | Get resting heart rate | GET |
| `get_menstrual_data_for_date(date)` | `/periodichealth-service/menstrualcycle/dayview/{date}` | Get menstrual data | GET |
| `get_menstrual_calendar_data(startdate, enddate)` | `/periodichealth-service/menstrualcycle/calendar/{startdate}/{enddate}` | Get menstrual calendar | GET |
| `get_pregnancy_summary()` | `/periodichealth-service/menstrualcycle/pregnancysnapshot` | Get pregnancy summary | GET |

---

### System & Export (4 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `query_garmin_graphql(query)` | `graphql-gateway/graphql` | Execute GraphQL query | POST |
| `get_request_reload(date)` | `/wellness-service/wellness/epoch/request/{date}` | Request data reload | POST |
| `disconnect()` | Internal | Disconnect/logout | - |
| `remove_tokens()` | Internal | Remove stored tokens | - |

---

### Training Plans (3 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_training_plans()` | `/trainingplan-service/trainingplan/plans` | Get training plans | GET |
| `get_training_plan_by_id(plan_id)` | `/trainingplan-service/trainingplan/phased/{plan_id}` | Get training plan | GET |
| `get_adaptive_training_plan_by_id(plan_id)` | `/trainingplan-service/trainingplan/fbt-adaptive/{plan_id}` | Get adaptive training plan | GET |

---

### Biometric Service (4 endpoints)

| Method | Endpoint | Description | HTTP Method |
|--------|----------|-------------|-------------|
| `get_lactate_threshold(...)` | `/biometric-service/biometric/latestLactateThreshold` | Get latest lactate threshold | GET |
| `get_cycling_ftp()` | `/biometric-service/biometric/latestFunctionalThresholdPower/CYCLING` | Get cycling FTP | GET |
| `get_power_to_weight_ratio(date)` | `/biometric-service/biometric/powerToWeight/latest/{date}?sport=Running` | Get power to weight ratio | GET |

---

## Notes

1. **Authentication**: All endpoints require OAuth authentication via tokens stored by the `garth` library.

2. **Date Format**: All date parameters use format `YYYY-MM-DD`.

3. **Base Service**: All endpoints are accessed through `/connectapi` service.

4. **Display Name**: Many endpoints require `{displayName}` which is obtained from the user profile during login.

5. **File Downloads**: Activity downloads use GET requests but require special handling (binary data).

6. **File Uploads**: Uploads use multipart/form-data with POST requests.

7. **Response Format**: Most endpoints return JSON. Download endpoints return binary data.

---

## Total API Endpoints Summary

- **Total Methods**: ~105+ unique API methods
- **HTTP GET**: ~85 methods
- **HTTP POST**: ~12 methods
- **HTTP PUT**: ~6 methods
- **HTTP DELETE**: ~3 methods

---

## References

- Base Library: garth (Garmin OAuth client)
- API Base: Garmin Connect Web Services


# Garmin Connect API - cURL Examples

This document provides cURL examples for all Garmin Connect API endpoints.

**Important Notes:**
- All endpoints require OAuth authentication tokens
- Base URL: `https://connect.garmin.com` (or `https://connect.garmin.cn` for China)
- All API calls go through `/connectapi` service
- Replace `{{token}}` with your actual OAuth token
- Replace `{{displayName}}` with your user's display name
- Replace date placeholders with actual dates in `YYYY-MM-DD` format

---

## Authentication

The library uses OAuth1/OAuth2 tokens. You'll need to obtain tokens using the `garth` library or authenticate via the web interface.

**For detailed authentication flow and login endpoints, see `AUTHENTICATION_API.md`**

### Quick Authentication Overview

1. **Get OAuth Consumer**: `https://thegarth.s3.amazonaws.com/oauth_consumer.json`
2. **OAuth1 Request Token**: `https://connectapi.garmin.com/oauth-service/oauth/request_token`
3. **User Login (SSO)**: `https://connect.garmin.com/sso/signin`
4. **OAuth1 Access Token**: `https://connectapi.garmin.com/oauth-service/oauth/access_token`
5. **OAuth2 Token Exchange**: `https://connectapi.garmin.com/oauth-service/oauth/exchange/user/2.0`

**Recommended**: Use the `python-garminconnect` library which handles authentication automatically:
```python
from garminconnect import Garmin
garmin = Garmin(email="...", password="...")
garmin.login()  # Handles entire OAuth flow
```

---

## User & Profile

### Get User Profile
```bash
curl -X GET "https://connect.garmin.com/connectapi/userprofile-service/userprofile/user-settings" \
  -H "Authorization: Bearer {{token}}"
```

### Get User Profile Settings
```bash
curl -X GET "https://connect.garmin.com/connectapi/userprofile-service/userprofile/settings" \
  -H "Authorization: Bearer {{token}}"
```

### Get User Profile Info
```bash
curl -X GET "https://connect.garmin.com/connectapi/userprofile-service/userprofile/profile" \
  -H "Authorization: Bearer {{token}}"
```

---

## Daily Health & Activity

### Get User Summary
```bash
curl -X GET "https://connect.garmin.com/connectapi/usersummary-service/usersummary/daily/{{displayName}}?calendarDate=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Steps Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/dailySummaryChart/{{displayName}}?date=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Floors
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/floorsChartData/daily/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Daily Steps (Range)
```bash
curl -X GET "https://connect.garmin.com/connectapi/usersummary-service/stats/steps/daily/2024-01-01/2024-01-31" \
  -H "Authorization: Bearer {{token}}"
```

### Get Heart Rates
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/dailyHeartRate/{{displayName}}?date=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Respiration Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/daily/respiration/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get SpO2 Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/daily/spo2/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Intensity Minutes
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/daily/im/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get All Day Stress
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/dailyStress/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

---

## Advanced Health Metrics

### Get Body Battery
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/bodyBattery/reports/daily?calendarDate=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Body Battery Events
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/bodyBattery/events/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get HRV Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/hrv-service/hrv/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Training Readiness
```bash
curl -X GET "https://connect.garmin.com/connectapi/metrics-service/metrics/trainingreadiness/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Endurance Score
```bash
curl -X GET "https://connect.garmin.com/connectapi/metrics-service/metrics/endurancescore?calendarDate=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Hill Score
```bash
curl -X GET "https://connect.garmin.com/connectapi/metrics-service/metrics/hillscore?calendarDate=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Race Predictions
```bash
curl -X GET "https://connect.garmin.com/connectapi/metrics-service/metrics/racepredictions/latest/{{displayName}}" \
  -H "Authorization: Bearer {{token}}"
```

### Get Training Status
```bash
curl -X GET "https://connect.garmin.com/connectapi/metrics-service/metrics/trainingstatus/aggregated/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get Fitness Age
```bash
curl -X GET "https://connect.garmin.com/connectapi/fitnessage-service/fitnessage/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

---

## Activities

### Get Activities
```bash
curl -X GET "https://connect.garmin.com/connectapi/activitylist-service/activities/search/activities?start=0&limit=20" \
  -H "Authorization: Bearer {{token}}"
```

### Get Activities Count
```bash
curl -X GET "https://connect.garmin.com/connectapi/activitylist-service/activities/count" \
  -H "Authorization: Bearer {{token}}"
```

### Get Activity
```bash
curl -X GET "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}"
```

### Get Activity Details
```bash
curl -X GET "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}/details?maxChartSize=2000&maxPolylineSize=4000" \
  -H "Authorization: Bearer {{token}}"
```

### Set Activity Name
```bash
curl -X PUT "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "activityId": "{{activityId}}",
    "activityName": "Updated Activity Name"
  }'
```

### Delete Activity
```bash
curl -X DELETE "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}"
```

### Download Activity (FIT)
```bash
curl -X GET "https://connect.garmin.com/connectapi/download-service/files/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}" \
  -o activity.fit
```

### Download Activity (GPX)
```bash
curl -X GET "https://connect.garmin.com/connectapi/download-service/export/gpx/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}" \
  -o activity.gpx
```

### Download Activity (TCX)
```bash
curl -X GET "https://connect.garmin.com/connectapi/download-service/export/tcx/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}" \
  -o activity.tcx
```

### Upload Activity
```bash
curl -X POST "https://connect.garmin.com/connectapi/upload-service/upload" \
  -H "Authorization: Bearer {{token}}" \
  -F "file=@/path/to/activity.fit"
```

### Get Activity Types
```bash
curl -X GET "https://connect.garmin.com/connectapi/activity-service/activity/activityTypes" \
  -H "Authorization: Bearer {{token}}"
```

### Get Activity Splits
```bash
curl -X GET "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}/splits" \
  -H "Authorization: Bearer {{token}}"
```

### Get Activity Weather
```bash
curl -X GET "https://connect.garmin.com/connectapi/activity-service/activity/{{activityId}}/weather" \
  -H "Authorization: Bearer {{token}}"
```

---

## Workouts

### Get Workouts
```bash
curl -X GET "https://connect.garmin.com/connectapi/workout-service/workouts?start=0&limit=100" \
  -H "Authorization: Bearer {{token}}"
```

### Get Workout by ID
```bash
curl -X GET "https://connect.garmin.com/connectapi/workout-service/workout/{{workoutId}}" \
  -H "Authorization: Bearer {{token}}"
```

### Download Workout (FIT)
```bash
curl -X GET "https://connect.garmin.com/connectapi/workout-service/workout/FIT/{{workoutId}}" \
  -H "Authorization: Bearer {{token}}" \
  -o workout.fit
```

### Upload Workout
```bash
curl -X POST "https://connect.garmin.com/connectapi/workout-service/workout" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "workoutName": "Test Workout",
    "sport": "running"
  }'
```

---

## Body Composition & Weight

### Get Weigh Ins (Range)
```bash
curl -X GET "https://connect.garmin.com/connectapi/weight-service/weight/range/2024-01-01/2024-01-31?includeAll=true" \
  -H "Authorization: Bearer {{token}}"
```

### Get Daily Weigh Ins
```bash
curl -X GET "https://connect.garmin.com/connectapi/weight-service/weight/dayview/2024-01-15?includeAll=true" \
  -H "Authorization: Bearer {{token}}"
```

### Add Weigh In
```bash
curl -X POST "https://connect.garmin.com/connectapi/weight-service/user-weight" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "value": 75.5,
    "unitKey": "kg",
    "dateTimestamp": "2024-01-15T10:00:00.000",
    "gmtTimestamp": "2024-01-15T10:00:00.000",
    "sourceType": "MANUAL"
  }'
```

### Delete Weigh In
```bash
curl -X DELETE "https://connect.garmin.com/connectapi/weight-service/weight/2024-01-15/byversion/{{weightPk}}" \
  -H "Authorization: Bearer {{token}}"
```

---

## Hydration & Wellness

### Get Hydration Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/usersummary-service/usersummary/hydration/daily/2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Add Hydration Data
```bash
curl -X PUT "https://connect.garmin.com/connectapi/usersummary-service/usersummary/hydration/log" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "calendarDate": "2024-01-15",
    "valueInML": 2000,
    "goalInML": 2500
  }'
```

### Get Sleep Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/dailySleepData/{{displayName}}?date=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Get All Day Events
```bash
curl -X GET "https://connect.garmin.com/connectapi/wellness-service/wellness/dailyEvents?calendarDate=2024-01-15" \
  -H "Authorization: Bearer {{token}}"
```

### Set Blood Pressure
```bash
curl -X POST "https://connect.garmin.com/connectapi/bloodpressure-service/bloodpressure" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "systolic": 120,
    "diastolic": 80,
    "pulse": 72,
    "timestamp": "2024-01-15T10:00:00.000"
  }'
```

### Get Blood Pressure (Range)
```bash
curl -X GET "https://connect.garmin.com/connectapi/bloodpressure-service/bloodpressure/range/2024-01-01/2024-01-31?includeAll=true" \
  -H "Authorization: Bearer {{token}}"
```

### Delete Blood Pressure
```bash
curl -X DELETE "https://connect.garmin.com/connectapi/bloodpressure-service/bloodpressure/2024-01-15/{{version}}" \
  -H "Authorization: Bearer {{token}}"
```

---

## Goals & Achievements

### Get Personal Records
```bash
curl -X GET "https://connect.garmin.com/connectapi/personalrecord-service/personalrecord/prs/{{displayName}}" \
  -H "Authorization: Bearer {{token}}"
```

### Get Earned Badges
```bash
curl -X GET "https://connect.garmin.com/connectapi/badge-service/badge/earned" \
  -H "Authorization: Bearer {{token}}"
```

### Get Available Badges
```bash
curl -X GET "https://connect.garmin.com/connectapi/badge-service/badge/available" \
  -H "Authorization: Bearer {{token}}"
```

### Get Goals
```bash
curl -X GET "https://connect.garmin.com/connectapi/goal-service/goal/goals" \
  -H "Authorization: Bearer {{token}}"
```

---

## Device & Technical

### Get Devices
```bash
curl -X GET "https://connect.garmin.com/connectapi/device-service/deviceregistration/devices" \
  -H "Authorization: Bearer {{token}}"
```

### Get Device Settings
```bash
curl -X GET "https://connect.garmin.com/connectapi/device-service/deviceservice/device-info/settings/{{deviceId}}" \
  -H "Authorization: Bearer {{token}}"
```

### Get Primary Training Device
```bash
curl -X GET "https://connect.garmin.com/connectapi/web-gateway/device-info/primary-training-device" \
  -H "Authorization: Bearer {{token}}"
```

### Get Device Solar Data
```bash
curl -X GET "https://connect.garmin.com/connectapi/web-gateway/solar/{{deviceId}}/2024-01-01/2024-01-31" \
  -H "Authorization: Bearer {{token}}"
```

### Get Device Last Used
```bash
curl -X GET "https://connect.garmin.com/connectapi/device-service/deviceservice/mylastused" \
  -H "Authorization: Bearer {{token}}"
```

---

## Gear & Equipment

### Get Gear
```bash
curl -X GET "https://connect.garmin.com/connectapi/gear-service/gear/filterGear?userProfilePk={{userProfileNumber}}" \
  -H "Authorization: Bearer {{token}}"
```

### Get Gear Stats
```bash
curl -X GET "https://connect.garmin.com/connectapi/gear-service/gear/stats/{{gearUUID}}" \
  -H "Authorization: Bearer {{token}}"
```

### Link Gear to Activity
```bash
curl -X PUT "https://connect.garmin.com/connectapi/gear-service/gear/link/{{gearUUID}}/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}"
```

### Unlink Gear from Activity
```bash
curl -X PUT "https://connect.garmin.com/connectapi/gear-service/gear/unlink/{{gearUUID}}/activity/{{activityId}}" \
  -H "Authorization: Bearer {{token}}"
```

---

## Training Plans

### Get Training Plans
```bash
curl -X GET "https://connect.garmin.com/connectapi/trainingplan-service/trainingplan/plans" \
  -H "Authorization: Bearer {{token}}"
```

### Get Training Plan by ID
```bash
curl -X GET "https://connect.garmin.com/connectapi/trainingplan-service/trainingplan/phased/{{planId}}" \
  -H "Authorization: Bearer {{token}}"
```

---

## GraphQL

### Query GraphQL
```bash
curl -X POST "https://connect.garmin.com/connectapi/graphql-gateway/graphql" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { userProfile { displayName } }",
    "variables": {}
  }'
```

---

## Notes on Authentication

The actual authentication mechanism uses OAuth1/OAuth2 tokens. The examples above show simplified Bearer token format. In practice:

1. **OAuth1 Token**: Used for some legacy endpoints
2. **OAuth2 Token**: Used for most modern endpoints
3. **Session Cookies**: May also be required

The `garth` library handles the OAuth flow automatically. For manual token extraction:

1. Log in via web browser to Garmin Connect
2. Extract OAuth tokens from browser session storage
3. Use tokens in API requests

**Security Warning**: Never expose tokens publicly. Store them securely and use environment variables or secure credential storage.

---

## Common Parameters

- **Dates**: Format `YYYY-MM-DD` (e.g., `2024-01-15`)
- **Timestamps**: Format `YYYY-MM-DDTHH:mm:ss.SSS` (e.g., `2024-01-15T10:00:00.000`)
- **Display Name**: Obtained from user profile (use `get_user_profile()` endpoint first)
- **Activity ID**: Numeric ID of an activity (found in activity list)
- **Workout ID**: Numeric ID of a workout
- **Device ID**: Numeric ID of a device
- **Gear UUID**: UUID string for gear items

---

## Error Handling

Common HTTP status codes:
- **200**: Success
- **401**: Unauthorized (invalid or expired token)
- **403**: Forbidden (insufficient permissions)
- **404**: Not found
- **429**: Too many requests (rate limit)
- **500**: Server error

If you receive 401 errors, refresh your OAuth tokens.



OUTPUT:
- Provide complete, compilable code
- Include edge cases handling
- No pseudocode
- No explanations unless asked

TESTS:
- Include basic tests or usage examples