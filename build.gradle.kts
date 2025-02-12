// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.8.0") // Use the latest stable version
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // Updated Kotlin version
        classpath ("com.google.gms:google-services:4.4.0") // Use the latest stable version
    }
}

plugins {
    id("com.android.application") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false // Updated Kotlin version
    id("com.google.gms.google-services") version "4.4.0" apply false
}
