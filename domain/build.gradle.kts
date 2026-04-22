plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    // Kotlin Dependency
    implementation(libs.kotlinx.coroutines.core)

    // JavaX Dependency
    implementation(libs.javax.inject)

    // Project Dependency
    implementation(project(":core:common"))
}