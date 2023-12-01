plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.y9san9.aoc"
version = "23"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}
