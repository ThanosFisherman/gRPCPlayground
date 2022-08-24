import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addImplementations() {

    Deps.Utils.getAll().forEach { implementation(it) }
    Deps.AndroidX.getAll().forEach { implementation(it) }
    Deps.Grpc.getAll().forEach { implementation(it) }
    Deps.Prefs.getAll().forEach { implementation(it) }
}

fun DependencyHandler.addTestDependencies() {
    Deps.Test.getAll().forEach { testImplementation(it) }
}

fun DependencyHandler.addAndroidTestDependencies() {
    Deps.AndroidTest.getAll().forEach { androidTestImplementation(it) }
}

/*
 * These extensions mimic the extensions that are generated on the fly by Gradle.
 * They are used here to provide above dependency syntax that mimics Gradle Kotlin DSL
 * syntax in module\build.gradle.kts files.
 */

// region dependency extensions

private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? = add(
    "implementation", dependencyNotation
)

private fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? = add(
    "debugImplementation", dependencyNotation
)

private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? = add(
    "testImplementation", dependencyNotation
)

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? = add(
    "androidTestImplementation", dependencyNotation
)

// endregion