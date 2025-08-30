plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.paint.randompeoplek"
    compileSdk = rootProject.extra["compile_sdk_version"] as Int

    defaultConfig {
        applicationId = rootProject.extra["application_id"] as String
        minSdk = rootProject.extra["min_sdk_version"] as Int
        targetSdk = rootProject.extra["target_sdk_version"] as Int
        versionCode = rootProject.extra["version_code"] as Int
        versionName = rootProject.extra["version_name"] as String
        testInstrumentationRunner = rootProject.extra["test_instrumentation_runner"] as String

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
        kotlinCompilerExtensionVersion = "1.5.11" // Consider moving this to rootProject.extra
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = rootProject.extra["jvm_target"] as String
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlin_version"]}")

    implementation("androidx.appcompat:appcompat:${rootProject.extra["appcompat_version"]}")
    implementation("androidx.core:core-ktx:${rootProject.extra["core_ktx_version"]}")
    implementation("androidx.core:core-splashscreen:${rootProject.extra["splash_screen_version"]}")
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintlayout_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.legacy:legacy-support-v4:${rootProject.extra["legacy_support_version"]}")
    implementation("androidx.recyclerview:recyclerview:${rootProject.extra["recyclerview_version"]}")
    implementation("androidx.coordinatorlayout:coordinatorlayout:${rootProject.extra["coordinatorlayout_version"]}")
    implementation("androidx.cardview:cardview:${rootProject.extra["cardview_version"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragment_ktx_version"]}")

    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit_version"]}")
    implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["retrofit_version"]}")

    implementation("com.google.dagger:hilt-android:${rootProject.extra["hilt_version"]}")
    implementation("com.google.android.material:material:${rootProject.extra["material"]}")
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hilt_version"]}")

    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    testImplementation("androidx.room:room-testing:${rootProject.extra["room_version"]}")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["nav_version"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["nav_version"]}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${rootProject.extra["nav_version"]}")
    implementation("androidx.navigation:navigation-compose:${rootProject.extra["nav_version"]}")
    implementation("androidx.hilt:hilt-navigation-compose:${rootProject.extra["hilt_nav_version"]}")
    androidTestImplementation("androidx.navigation:navigation-testing:${rootProject.extra["nav_version"]}")

    // compose
    val composeBom = platform("androidx.compose:compose-bom:2025.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material:material:${rootProject.extra["compose_material"]}")
    implementation("androidx.activity:activity-compose:${rootProject.extra["compose_activity"]}")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("io.coil-kt:coil-compose:2.7.0")
    // compose

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["kotlinx_coroutines_version"]}")

    testImplementation("junit:junit:${rootProject.extra["junit_version"]}")

    testImplementation("io.mockk:mockk:${rootProject.extra["mockK"]}")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutines_test"]}")
    testImplementation("androidx.arch.core:core-testing:${rootProject.extra["androidx_core_testing"]}")
    testImplementation("app.cash.turbine:turbine:${rootProject.extra["turbine"]}")

    androidTestImplementation("androidx.test:runner:${rootProject.extra["test_runner_version"]}")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:core:1.4.0")
}
