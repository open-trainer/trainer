plugins {
}

group = "com.opentrainer"
version = "1.0-SNAPSHOT" // or move to gradle.properties

subprojects {
    apply(plugin = "java")

    repositories { mavenCentral() }

    // Use java-library for library modules (see point 3)

    extensions.configure(JavaPluginExtension::class.java) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}