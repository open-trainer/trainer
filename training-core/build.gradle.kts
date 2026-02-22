plugins {
    java
}

group = "com.opentrainer"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":training-domain"))
    implementation(libs.spring.security.crypto)
    implementation(libs.spring.context)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}