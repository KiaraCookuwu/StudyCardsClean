// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // KSP para Kotlin 2.0.21
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    // CORRECCIÓN CRÍTICA: Usamos 2.51.1 para coincidir con el módulo app
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}