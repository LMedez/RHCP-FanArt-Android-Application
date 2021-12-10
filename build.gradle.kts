// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.gmsPlugin)
        classpath(BuildPlugins.safeArgsPlugin)

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}