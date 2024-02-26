// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("io.gitlab.arturbosch.detekt") version("1.23.3")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.22")
    }
}

detekt {
    toolVersion = "1.18.1"
    source = files("src/main/kotlin", "src/test/kotlin")
    config = files("$rootDir/detekt.yml")
    parallel = true
    buildUponDefaultConfig = true
}
