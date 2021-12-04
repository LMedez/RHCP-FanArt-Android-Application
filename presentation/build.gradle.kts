plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
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

    implementation(project(":domain"))
    implementation(project(":musicservice"))
    implementation(Libraries.ktxCore)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.viewModel)
    implementation(Libraries.liveData)
    implementation(Libraries.koinAndroid)


    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.archTesting)
    testImplementation(TestLibraries.truth)
    testImplementation(TestLibraries.coroutineTest)
    androidTestImplementation(TestLibraries.testExtJunit)
    androidTestImplementation(TestLibraries.espressoCore)

    implementation ("androidx.media:media:1.4.2")
}
