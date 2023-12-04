pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    kotlin("jvm") version "1.9.20" apply false
}

rootProject.name = "aoc23"

include("native")
