plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "org.opentrainer"
version = "0.0.1-SNAPSHOT"
description = "garmin-client"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.configuration.processor)

    // Resilience4j for circuit breaker, rate limiting, retry
    implementation(libs.bundles.resilience4j)

    // OAuth support
    implementation(libs.spring.security.oauth2.client)

    implementation(libs.bundles.jackson)

    // Gson
    implementation(libs.gson)

    // OkHttp
    implementation(libs.okhttp)

    // Lombok for reducing boilerplate
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Validation
    implementation(libs.spring.boot.starter.validation)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.reactor.test)
    testImplementation(libs.mockwebserver)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// This is a library, not an executable application
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
