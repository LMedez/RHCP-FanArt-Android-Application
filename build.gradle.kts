// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath (BuildPlugins.androidGradlePlugin)
        classpath (BuildPlugins.kotlinGradlePlugin)
        classpath (BuildPlugins.gmsPlugin)
        classpath(BuildPlugins.safeArgsPlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}