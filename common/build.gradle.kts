
plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.parcelizePlugin)
    id(BuildPlugins.kotlinKapt)
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

}

dependencies {
    implementation(Libraries.firestore)
    implementation(Libraries.room)
    kapt(Libraries.roomCompiler)
    implementation(Libraries.roomKtx)
    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testExtJunit)
    androidTestImplementation(TestLibraries.espressoCore)
}