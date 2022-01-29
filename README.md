
# About

**This task helps to download one or multiple files with downloading progress for each file depending on the following:**

### Architecture
* [Clean Architecture](https://www.amazon.com/Clean-Architecture-Craftsmans-Software-Structure/dp/0134494164)
* [MVVM](https://www.raywenderlich.com/8984-mvvm-on-android)

### Patterns
* [Repository](https://developer.android.com/jetpack/docs/guide)
* [Observer](https://code.tutsplus.com/tutorials/android-design-patterns-the-observer-pattern--cms-28963)

### Approaches
* [SOLID Principle](https://itnext.io/solid-principles-explanation-and-examples-715b975dcad4?gi=79443348411d)


### Technology Stack
* [Kotlin](https://kotlinlang.org/)
* [View Binding](https://developer.android.com/topic/libraries/view-binding)
* [Dagger 2](https://github.com/google/dagger)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit 2](https://square.github.io/retrofit/)
* [Timber](https://github.com/JakeWharton/timber)
* [OkHttp](https://square.github.io/okhttp/)
* [Android Jetpack](https://developer.android.com/jetpack)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [ConstraintLayout](https://developer.android.com/training/constraint-layout)
* [Dexter](https://github.com/Karumi/Dexter)

### Layers
* Data(This layer is responsible to hold models)
* Domain(Retrieves data from data layer and dispatches to view layer. No connection with Android framework.)
* Base(Contains common stuff for other layers)
* UI(Responsible for UI stuff such as displaying data)

