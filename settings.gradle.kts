pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    kotlin("jvm") version "1.9.20" apply false
    kotlin("plugin.serialization") version "1.9.21" apply false
    id("org.jetbrains.kotlinx.benchmark") version "0.4.4" apply false
    id("me.champeau.jmh") version "0.7.2" apply false
}

rootProject.name = "aoc23"

include("native")
include("recycling-structures")
