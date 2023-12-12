plugins {
    kotlin("jvm")
    application
}

group = "me.y9san9.aoc"
version = "23"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":recycling-structures"))
    implementation(project(":geometry-2d"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}
