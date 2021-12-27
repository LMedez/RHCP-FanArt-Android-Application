<h1 align="center">RHCP Fan art application</h1>
<p align="center">
This Android application is a music streaming of Red hot chili peppers artist. It is built around the <a href="https://github.com/android/uamp">Universal Android Music Player </a>
<br></br>
<img src="screenshots/screenshot.gif" alt="Screenshot"/>
 
</p>

## Features
* 100% Kotlin
* MVVM architecture
* Reactive pattern
* Android Architecture Components
* Single activity pattern
* Dependency injection
* Testing (Upcoming)

## Tech Stacks
* [Koin](https://insert-koin.io/) - Dependency injection.
 * [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collections of libraries that help you design rebust, testable and      maintainable apps.
     * [Room](https://developer.android.com/training/data-storage/room) - Local persistence database.
     * [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) - UI related data holder, lifecycle aware.
     * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Observable data holder that notify views when underlying data changes.
     * [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declarative way to bind data to UI layout.
     * [View Binding](https://developer.android.com/topic/libraries/view-binding?hl=es-419) - Bind the view file to class controller
     * [Navigation component](https://developer.android.com/guide/navigation) - Fragment routing handler.
* [Flow](https://developer.android.com/kotlin/flow) Stream of value that returns from suspend function.
* [Coroutine](https://developer.android.com/kotlin/coroutines) Concurrency design pattern for asynchronous programming.
* [Glide](https://github.com/bumptech/glide) - Image loading.

## Architectures
![MVVM](screenshots/Architecture.jpeg)

This follow Google recommended [Guide to app architecture](https://developer.android.com/jetpack/guide) to structure our architecture based on MVVM, reactive UI using LiveData/Flow observables with data binding and view binding.

* **View**: Activity/Fragment with UI-specific logics only.
* **ViewModel**: It keeps the logic away from View layer, provides data streams for UI and handle user interactions.
* **Model**: Repository pattern, data layers that provide interface to manipulate data from both the local and remote data sources. The local data sources will serve as [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth).

The project is Multimodule and each module has its own build.gradle file. All dependencies are managed by the [Dependency.kt](https://github.com/LMedez/RHCPFanArtAndroidApplication/blob/master/buildSrc/src/main/kotlin/Dependencies.kt)


