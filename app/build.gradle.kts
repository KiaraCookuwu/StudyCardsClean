plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt") // Obligatorio para Hilt en esta configuración
}

android {
    namespace = "com.itvo.studycardsclean"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.itvo.studycardsclean"
        minSdk = 26
        targetSdk = 36
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
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // --- ICONOS ---
    implementation("androidx.compose.material:material-icons-extended:1.7.0")

    // --- NAVEGACIÓN ---
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // --- HILT (La parte crítica) ---
    implementation("com.google.dagger:hilt-android:2.51.1")
    // Usamos KAPT para el compilador de Hilt (Soluciona el error rootComponentPackage)
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // --- DATASTORE ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // --- ROOM (Base de Datos) ---
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    // Usamos KSP para Room (Esto es correcto y rápido)
    ksp("androidx.room:room-compiler:$room_version")

    // --- IMÁGENES (Coil) ---
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- SERIALIZACIÓN ---
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // --- LIFECYCLE ---
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Configuración para corregir tipos en KAPT
kapt {
    correctErrorTypes = true
}