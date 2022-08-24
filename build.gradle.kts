buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
        //classpath ("com.google.protobuf:protobuf-gradle-plugin:${Versions.protobuf}")
    }
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
