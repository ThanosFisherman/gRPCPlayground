import kotlin.reflect.full.memberProperties

object Versions {
    const val protobufVersion = "3.21.2"
    const val datastoreVersion = "1.0.0"
    const val prefsVersion = "1.2.0"
    const val gradle = "7.2.2"
    const val kotlin = "1.7.10"
    const val coreKtx = "1.8.0"
    const val appcompat = "1.4.2"
    const val material = "1.7.0-alpha02"
    const val lifecycle = "2.5.0"
    const val timber = "5.0.1"
    const val androidXJunit = "1.1.3"
    const val junit4 = "4.13.2"
    const val espresso = "3.4.0"
    const val navigation = "2.5.0"
    const val protobuf = "0.8.19"
    const val grpc = "1.47.0"
    const val annotationsApache = "6.0.53"
    const val coroutines = "1.6.3"
    const val koin = "3.2.0"
}

object Deps {

    object Utils {
        const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val koin = "io.insert-koin:koin-android:${Versions.koin}"
        const val koinNavGraph = "io.insert-koin:koin-androidx-navigation:${Versions.koin}"

        fun getAll() = Utils::class.memberProperties.filter { it.isConst }
            .map { it.getter.call().toString() }.toSet()
    }

    object Prefs {

        const val sharedPrefs = "androidx.preference:preference-ktx:${Versions.prefsVersion}"

        //const val datastorePrefs = "androidx.datastore:datastore-preferences:${Versions.datastoreVersion}"
        const val datastoreProto = "androidx.datastore:datastore:${Versions.datastoreVersion}"
        const val protobuf = "com.google.protobuf:protobuf-javalite:${Versions.protobufVersion}"
        //const val datastoreCore = "androidx.datastore:datastore-core:${Versions.datastoreVersion}"

        fun getAll() = Prefs::class.memberProperties
            .filter { it.isConst }
            .map { it.getter.call().toString() }
            .toSet()
    }

    object Grpc {
        const val grpcOkHttp = "io.grpc:grpc-okhttp:${Versions.grpc}"
        const val grpcProtobufLite = "io.grpc:grpc-protobuf-lite:${Versions.grpc}"
        const val grpcStub = "io.grpc:grpc-stub:${Versions.grpc}"
        const val apacheAnnotations =
            "org.apache.tomcat:annotations-api:${Versions.annotationsApache}"

        fun getAll() = Grpc::class.memberProperties.filter { it.isConst }
            .map { it.getter.call().toString() }.toSet()
    }

    object Test {
        const val junit4 = "junit:junit:${Versions.junit4}"

        fun getAll() = Test::class.memberProperties.filter { it.isConst }
            .map { it.getter.call().toString() }.toSet()
    }

    object AndroidTest {
        const val androidXJunit = "androidx.test.ext:junit:${Versions.androidXJunit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

        fun getAll() = AndroidTest::class.memberProperties.filter { it.isConst }
            .map { it.getter.call().toString() }.toSet()
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val navigationFragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

        fun getAll() = AndroidX::class.memberProperties.filter { it.isConst }
            .map { it.getter.call().toString() }.toSet()
    }
}