import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
}

android {
    namespace = "com.hecosw.trailtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hecosw.trailtracker"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }
        val keyName = "TRAILTRACKER_FLICKR_API_KEY"
        val apiKey = System.getenv(keyName) ?: localProperties.getProperty(keyName, "")
        buildConfigField("String", keyName, apiKey)

        buildConfigField("String", "MINIMUM_KILOMETERS_FOR_NEW_PHOTO", "\"0.1\"")
        buildConfigField("String", "FLICKR_API_BASE_URL", "\"https://api.flickr.com/services/rest/\"")
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // TODO: Hack just for the sake of this exercise, for testing in Release.
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android core, activity and lifecyle
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    val lifecycleRuntimeVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleRuntimeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleRuntimeVersion") // StateFlow.collectAsStateWithLifecycle()

    // Permission requests
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // Timber (logging)
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Coil (async image loading and caching)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Location
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Jetpack Compose (declarative and reactive UI framework)
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ViewModel (AAC MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Hilt (dependency injection)
    val daggerHiltAndroidVersion = "2.50"
    kapt("com.google.dagger:hilt-android-compiler:$daggerHiltAndroidVersion")
    implementation("com.google.dagger:hilt-android:$daggerHiltAndroidVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Arrow (Either and other functional utilities)
    implementation("io.arrow-kt:arrow-core:1.2.1")

    // Sandwich (ramework-independent ApiResponse)
    implementation("com.github.skydoves:sandwich-ktor:2.0.5")

    // Ktor client (REST API client)
    val ktorVersion = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Chucker (visual network debugger)
    val chuckerVersion = "4.0.0"
    debugImplementation("com.github.chuckerteam.chucker:library:$chuckerVersion")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:$chuckerVersion")

    // LeakCanary (memory leak detector)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")

    // Testing
    val jupiterVersion = "5.10.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("io.mockk:mockk:1.13.9")
}

kapt {
    correctErrorTypes = true // Hilt
}
