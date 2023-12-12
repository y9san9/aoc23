plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvm()

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
