plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}
group = "com.opentrainer"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java")

    repositories { mavenCentral() }

    extensions.configure(JavaPluginExtension::class.java) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}
