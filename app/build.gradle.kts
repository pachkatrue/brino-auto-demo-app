plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "ru.brinoauto.demo"
    compileSdk = 35
    defaultConfig {
        applicationId = "ru.brinoauto.demo"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
}
dependencies { testImplementation("junit:junit:4.13.2") }
