plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1") // ViewModel for Jetpack Compose
    implementation ("androidx.navigation:navigation-compose:2.7.3") // Navigation Component
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit for API calls
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // JSON conversion
    implementation ("io.coil-kt:coil-compose:2.4.0") // Coil for image loading
    implementation(libs.androidx.storage) // Image loading (for weather icons)
    implementation ("androidx.datastore:datastore-preferences:1.0.0") // DataStore for storing preferences
    implementation ("com.airbnb.android:lottie-compose:6.0.0") // Lottie for animations
    implementation ("com.google.android.gms:play-services-location:21.0.1") // Location services
    implementation ("androidx.work:work-runtime-ktx:2.8.1") // WorkManager for background tasks
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}