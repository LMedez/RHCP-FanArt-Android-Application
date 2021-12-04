plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
}
apply("../../build-variant.gradle")

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
}

dependencies {

    implementation(project(":presentation"))
    implementation(project(":core"))
    implementation(project(":common"))
    implementation(project(":resources"))

    implementation(Libraries.glide)
    implementation(Libraries.navigationComponentFragmentKtx)
    implementation(Libraries.koinAndroid)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.viewModel)
    implementation(Libraries.ktxCore)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testExtJunit)
    androidTestImplementation(TestLibraries.espressoCore)

}