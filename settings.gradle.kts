dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

rootProject.name = "RHCP Fan Art"
//Include all the existent modules in the project
rootDir
        .walk()
        .maxDepth(1)
        .filter {
            it.name != "buildSrc" && it.isDirectory &&
                    file("${it.absolutePath}/build.gradle.kts").exists()
        }
        .forEach {
            include(":${it.name}")
        }

include(":features:home")
include(":features:albumdetail")
include(":features:mediaplayer")
include(":features:mymusic")