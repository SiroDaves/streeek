plugins {
    id("bizilabs.convention.library")
    id("bizilabs.convention.compose.library")
}

android {
    namespace = "com.bizilabs.streeek.lib.design"
}

dependencies {

    /**
     * MODULES
     */
    implementation(projects.modulesUi.resources)

    /**
     * LIBRARIES
     */

    // splash screen
    implementation(libs.androidx.core.splashscreen)

}