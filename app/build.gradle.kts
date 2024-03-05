import com.android.build.api.dsl.Packaging

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navSafeArgs)
    id("kotlin-parcelize")
}

android {
    namespace = "com.vungn.backvietlibrary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vungn.backvietlibrary"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://thuvien.iptech.edu.vn/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://thuvien.iptech.edu.vn/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    packagingOptions {
        resources.excludes.apply {
            add("META-INF/LICENSE")
            add("META-INF/*.properties")
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

dependencies {
    // Worker
    implementation(libs.worker)
    implementation(libs.hilt.worker)
    ksp(libs.hilt.worker.compiler)

    // Room
    implementation(libs.room)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // OkHttp
    implementation(libs.okhttp.logging.interceptor)

    // circleimageview
    implementation(libs.circleimageview)

    // Datastore
    implementation(libs.datastore)

    // Gson
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Facebook
    implementation(libs.facebook)

    // Image Zoom
    implementation(libs.imagezoom)

    // Pdf Viewer
    implementation(libs.pdfviewer)

    // Palette
    implementation(libs.palette)

    // Glide
    implementation(libs.glide)
    implementation(libs.play.services.basement)
    annotationProcessor(libs.glide.compiler)

    // Nav
    implementation(libs.nav.fragment)
    implementation(libs.nav.ui.ktx)
    androidTestImplementation(libs.nav.test)
//    implementation(libs.nav.safe.args)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}