plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.destination"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.destination"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.room:room-common:2.6.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.hbb20:ccp:2.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    dependencies {
        // FirebaseUI for Firebase Realtime Database
        implementation ("com.firebaseui:firebase-ui-database:8.0.2")

        // FirebaseUI for Cloud Firestore
        implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")

        // FirebaseUI for Firebase Auth
        implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

        // FirebaseUI for Cloud Storage
        implementation ("com.firebaseui:firebase-ui-storage:8.0.2")
        implementation ("com.github.dhaval2404:imagepicker:2.1")
        implementation("com.github.dhaval2404:imagepicker:2.1")
        implementation("com.github.bumptech.glide:glide:4.16.0")

        implementation("com.firebaseui:firebase-ui-firestore: 8.0.2")


        implementation("de.hdodenhof:circleimageview:3.1.0")

        implementation("com.karumi:dexter:6.2.3")
        implementation("androidx.room:room-runtime:2.6.1")
        annotationProcessor("androidx.room:room-compiler:2.6.1")
///////////////
        implementation("com.github.yalantis:ucrop:2.2.8")
        //////////
        implementation("com.github.yalantis:ucrop:2.2.8-native")
        ///
        //implementation("com.github.krokyze:ucropnedit:2.2.6-2")
        /////////map
        implementation ("com.google.android.gms:play-services-maps:18.2.0")

        implementation ("com.google.android.gms:play-services-location:21.2.0")

        implementation("com.google.firebase:firebase-database")

        implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

        implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

        implementation("com.airbnb.android:lottie:3.7.0")




    }
}