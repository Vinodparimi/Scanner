plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    id("com.google.gms.google-services") // Google services plugin
}

android {
    namespace = "com.example.s3388461"
    compileSdk = 34  // Ensure it's aligned with your target SDK

    defaultConfig {
        applicationId = "com.example.s3388461"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // ✅ Core AndroidX Libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // ✅ Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")


    // ✅ Material (NOT Material3)
    implementation("androidx.compose.material:material")  // Fixes the theme issue

    // ✅ Navigation Component for Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ✅ Firebase BOM (Ensures Latest Versions)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // ✅ DataStore for Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ✅ WorkManager for Background Tasks
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // ✅ Coil for Image Loading
    implementation("io.coil-kt:coil-compose:2.2.2")

    // ✅ Material Icons for Compose
    implementation("androidx.compose.material:material-icons-extended:1.5.1")

    // ✅ AppCompat Support
    implementation("androidx.appcompat:appcompat:1.6.1")

    // ✅ Play Services Authentication (for Google Sign-in)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation(libs.androidx.material3.android)

    // ✅ Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
