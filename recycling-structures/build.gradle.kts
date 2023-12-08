plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    jvm()
}

dependencies {
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}
