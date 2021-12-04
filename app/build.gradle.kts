plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.googleServices)
    id(BuildPlugins.safeArgs)

}

apply("../build-variant.gradle")

android {

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        aaptOptions.cruncherEnabled = false
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
        }
    }

    compileSdk = AndroidSdk.target

    defaultConfig {
        applicationId = AndroidClient.appId
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = 1
        versionName = "0.0.1-alpha"
        multiDexEnabled = true
        testInstrumentationRunner = AndroidClient.testRunner
    }

    viewBinding {
        isEnabled = true
    }

    dataBinding {
        isEnabled = true
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

    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":resources"))
    implementation(project(":musicservice"))
    implementation(project(":features:home"))
    implementation(project(":features:albumdetail"))
    implementation(project(":features:mediaplayer"))
    implementation(project(":features:mymusic"))

    implementation(Libraries.koinAndroid)
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.navigationComponentUI)
    implementation(Libraries.navigationComponentFragment)
    implementation(Libraries.ktxCore)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.playCore)
    implementation(Libraries.playCoreKtx)
    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testExtJunit)
    androidTestImplementation(TestLibraries.espressoCore)
}