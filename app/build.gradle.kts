plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.e2145148_weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.e2145148_weather"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Adding necessary dependencies for weather application
    implementation(libs.volley)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.location)


}

