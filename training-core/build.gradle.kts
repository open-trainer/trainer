plugins {
    id("java")
}

group = "com.opentrainer"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":training-domain"))
    implementation("org.springframework.security:spring-security-crypto:6.3.3")
    implementation("org.springframework:spring-context:6.1.13")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}