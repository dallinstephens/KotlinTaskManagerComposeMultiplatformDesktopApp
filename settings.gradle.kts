rootProject.name = "kotlinDesktopApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // ADD THIS NEW REPOSITORY FOR THE COMPOSE COMPILER PLUGIN
        maven("https://maven.pkg.jetbrains.space/public/p/compose/maven") // <--- ADD THIS LINE
    }
}

dependencyResolutionManagement {
    // This line is important for ensuring Gradle only uses repositories defined here
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Simplify google() and remove restrictive content filter
        google()
        mavenCentral()
        // THIS IS CRUCIAL FOR JETBRAINS COMPOSE MULTIPLATFORM LIBRARIES
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")