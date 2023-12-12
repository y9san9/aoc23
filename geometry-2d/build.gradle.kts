plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvm()
}
