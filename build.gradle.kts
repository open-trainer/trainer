plugins {
}

group = "com.opentrainer"
version = "1.0-SNAPSHOT" // or move to gradle.properties

subprojects {
    apply(plugin = "java")

    repositories { mavenCentral() }

    extensions.configure(JavaPluginExtension::class.java) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}