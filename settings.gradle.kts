rootProject.name = "pcaProtocol"

include(":core")

listOf(
        "v1_17_1",
        "v1_18",
        "v1_19", "v1_19_1", "v1_19_3", "v1_19_4",
        "v1_20", "v1_20_2", "v1_20_3", "v1_20_6",
        "v1_21", "v1_21_3", "v1_21_4", "v1_21_5"
).forEach {
    include(":$it")
    project(":$it").projectDir = file("nms/$it")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}