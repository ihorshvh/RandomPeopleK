plugins {
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = libs.versions.testInstrumentationRunner.get()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.bundles.retrofit)

    implementation(libs.hilt.android)
    implementation(libs.material.components)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.arch.core.testing)

    // navigation
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.hilt.navigation.compose)
    androidTestImplementation(libs.androidx.navigation.testing)

    // compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.bundles.compose.ui)
    // Android Studio Preview support
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.manifest)
    implementation(libs.coil.compose)
    // compose
    implementation(libs.bundles.coroutines)

    testImplementation(libs.bundles.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)

    androidTestImplementation(libs.bundles.androidx.test)
}