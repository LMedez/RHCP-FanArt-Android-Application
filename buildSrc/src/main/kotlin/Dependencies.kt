object Kotlin {
    const val standardLibrary = "1.5.31"
    const val coroutines = "1.3.9"
}

object AndroidSdk {
    const val min = 21
    private const val compile = 31
    const val target = compile
}

object AndroidClient {
    const val appId = "com.luc.rhcpfanart"
    const val versionCode = 1
    const val versionName = "1.0"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "7.0.2"
        const val gradleVersion = "6.8"
        const val hilt = "2.38.1"
        const val gms = "4.3.10"
        const val safeArgs = "2.3.5"
    }

    const val parcelizePlugin = "kotlin-parcelize"
    const val safeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safeArgs}"
    const val safeArgs = "androidx.navigation.safeargs.kotlin"
    const val gmsPlugin = "com.google.gms:google-services:${Versions.gms}"
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val googleServices = "com.google.gms.google-services"
    const val androidHilt = "dagger.hilt.android.plugin"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val variants = "scripts.build-variants"
    const val quality = "scripts.quality"
    const val compilation = "scripts.compilation"
}


object Libraries {
    private object Versions {
        const val room = "2.3.0"
        const val navigation = "2.3.5"
        const val firestore = "23.0.3"
        const val firestoreCoroutines = "1.1.1"
        const val hilt = BuildPlugins.Versions.hilt
        const val appCompat = "1.3.1"
        const val constraintLayout = "2.1.0"
        const val recyclerView = "1.1.0"
        const val cardView = "1.0.0"
        const val material = "1.5.0-alpha05"
        const val lifecycle = "2.2.0"
        const val lifecycleExtensions = "2.1.0"
        const val annotations = "1.1.0"
        const val ktx = "1.6.0"
        const val koin = "3.1.2"
        const val glide = "4.12.0"
        const val retrofit = "2.9.0"
        const val okHttpLoggingInterceptor = "4.9.0"
        const val fragmentKtx = "1.3.6"
        const val exoplayer = "2.15.1"
        const val playCore = "1.10.2"
        const val playCoreKtx = "1.8.1"
    }

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"
    const val exoplayerUI = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"
    const val exoplayerMediasession =
        "com.google.android.exoplayer:extension-mediasession:${Versions.exoplayer}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val navigationComponentFragment =
        "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigationComponentFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationComponentUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val smartRecyclerAdapter = "io.github.manneohlund:smart-recycler-adapter:5.0.0-rc01"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Kotlin.coroutines}"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Kotlin.coroutines}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val firestoreCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.firestoreCoroutines}"
    const val firestore = "com.google.firebase:firebase-firestore-ktx:${Versions.firestore}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val androidAnnotations = "androidx.annotation:annotation:${Versions.annotations}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val retrofit = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okHttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLoggingInterceptor}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
    const val playCoreKtx = "com.google.android.play:core-ktx:${Versions.playCoreKtx}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.13.1"
        const val mockk = "1.10.0"
        const val robolectric = "4.4"
        const val kluent = "1.14"
        const val testRunner = "1.1.0"
        const val arch = "1.1.1"
        const val espressoCore = "3.4.0"
        const val espressoIntents = "3.1.0"
        const val testExtensions = "1.1.3"
        const val testRules = "1.1.0"
        const val hiltTesting = BuildPlugins.Versions.hilt
        const val truth = "1.1.3"
        const val coroutine = "1.4.2"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}"
    const val archTesting = "android.arch.core:core-testing:${Versions.arch}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val espressoIntents =
        "androidx.test.espresso:espresso-intents:${Versions.espressoIntents}"
    const val testExtJunit = "androidx.test.ext:junit:${Versions.testExtensions}"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.hiltTesting}"
}
