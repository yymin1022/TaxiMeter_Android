plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Kotlin Dependency
    implementation(libs.kotlinx.coroutines.core)

    // JavaX Dependency
    implementation(libs.javax.inject)

    // Project Dependency
    implementation(project(":core:common"))
}