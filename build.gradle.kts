// Top-level build file where you can add configuration options common to all sub-projects/modules.

import org.gradle.api.tasks.Delete

// Define versions at the top
val kotlin_version_val = "2.1.10"
val devtools_ksp_val = "2.0.21-1.0.27"
val android_gradle_plugin_version = "8.9.2"
val hilt_version_val = "2.52"

extra.apply {
    set("min_sdk_version", 23)
    set("target_sdk_version", 36)
    set("compile_sdk_version", 36)
    set("build_tools_version", "34.0.0")
    set("jvm_target", "17")

    set("application_id", "com.paint.randompeoplek")
    set("version_code", 1)
    set("version_name", "1.0")
    set("test_instrumentation_runner", "androidx.test.runner.AndroidJUnitRunner")

    set("kotlin_version", "2.1.10")
    set("appcompat_version", "1.7.1")
    set("core_ktx_version", "1.17.0")
    set("splash_screen_version", "1.0.1")
    set("constraintlayout_version", "2.2.1")
    set("lifecycle_version", "2.9.2")
    set("legacy_support_version", "1.0.0")
    set("recyclerview_version", "1.4.0")
    set("coordinatorlayout_version", "1.3.0")
    set("cardview_version", "1.0.0")
    set("fragment_ktx_version", "1.8.9")
    set("retrofit_version", "2.9.0")
    set("hilt_version", "2.52")
    set("room_version", "2.7.2")
    set("glide_version", "4.12.0")
    set("kotlinx_coroutines_version", "1.6.1")
    set("junit_version", "4.13.2")
    set("mockK", "1.13.12")
    set("test_runner_version", "1.3.0")
    set("espresso_version", "3.1.0")
    set("nav_version", "2.9.3")
    set("hilt_nav_version", "1.2.0")
    set("coroutines_test", "1.5.2")
    set("material", "1.12.0")
    set("compose_material", "1.6.4")
    set("compose_activity", "1.10.1")
    set("androidx_core_testing", "2.2.0")
    set("turbine", "1.1.0")
    set("devtools_ksp", "2.0.21-1.0.27")
}

plugins {
    id("com.android.application") version "8.9.2" apply false
    id("com.android.library") version "8.9.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
}
