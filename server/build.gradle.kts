import com.google.protobuf.gradle.*

plugins {
    java
    kotlin("jvm") version "1.7.10"
    id("com.google.protobuf") version "0.8.19"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.5"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.47.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

dependencies {
//    implementation("io.grpc:grpc-okhttp:1.47.0")
//    implementation("org.apache.tomcat:annotations-api:6.0.53")

    implementation("io.grpc:grpc-netty:1.47.0")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    implementation("io.grpc:grpc-protobuf:1.47.0")
    implementation("com.google.protobuf:protobuf-kotlin:3.21.5")
    testImplementation("junit:junit:4.13.2")
}

tasks.register<JavaExec>("grpcServer") {
    dependsOn("classes")
    dependsOn("generateProto")
    dependsOn("assemble")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.thanosfisherman.grpcplayground.server.MainKt")
}