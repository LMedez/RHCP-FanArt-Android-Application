plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.safeArgs)
}
apply("../build-variant.gradle")

android {

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
    }

    compileSdk = AndroidSdk.target

    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        testInstrumentationRunner = AndroidClient.testRunner
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        isEnabled = true
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(Libraries.navigationComponentFragment)
    implementation(project(":common"))

    implementation(Libraries.material)
    implementation(Libraries.glide)
    implementation(Libraries.appCompat)
    implementation(Libraries.ktxCore)
}
