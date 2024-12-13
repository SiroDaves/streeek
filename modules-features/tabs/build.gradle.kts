import org.gradle.kotlin.dsl.android

plugins {
    id("bizilabs.convention.feature")
}

android {
    namespace = "com.bizilabs.streeek.feature.tabs"
}

dependencies {
    // coil
    implementation(libs.bundles.coil)
}