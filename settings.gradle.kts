plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "trainer"

include("garmin-client")
include("training-core")
include("training-domain")
include("training-clients")
include("training-infra")
include("training-api")

include("user-api")
include("user-infra")