FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy the whole repository (build context should be repo root)
COPY . .

# Build the training-api boot jar
RUN gradle :training-api:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the correct jar for training-api
COPY --from=build /app/training-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
