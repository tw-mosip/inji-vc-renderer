plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
}

android {
    namespace = "io.mosip.injivcrenderer"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
apply(file("local-publish-artifact.gradle"))
tasks {
    register<Wrapper>("wrapper") {
        gradleVersion = "8.5"
    }
}

    tasks.register<Jar>("jarRelease") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn("assembleRelease")
        from("build/intermediates/javac/release/classes") {
            include("**/*.class")
        }
        from("build/tmp/kotlin-classes/release") {
            include("**/*.class")
        }
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = "1.5-SNAPSHOT"
        }
        archiveBaseName.set("${project.name}-release")
        archiveVersion.set("1.5-SNAPSHOT")
        destinationDirectory.set(layout.buildDirectory.dir("libs"))
    }
    tasks.register("generatePom") {
        dependsOn("generatePomFileForAarPublication", "generatePomFileForJarReleasePublication")
    }