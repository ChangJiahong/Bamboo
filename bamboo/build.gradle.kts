plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    namespace = "cn.changjiahong.bamboo"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
//        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
//        getByName("release")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        // for view binding :
        viewBinding = true
        dataBinding = true
    }

    // Use KSP Generated sources
    libraryVariants.all { variant ->
        variant.addJavaSourceFoldersToModel(file("build/generated/ksp/${variant.name}/kotlin"))
        true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("com.google.android.material:material:1.6.1")

    api(libs.viewbinding)
    api(libs.bundles.databinding)
    api(libs.bundles.lifecycle)
    api(libs.eventbus)
    api(libs.activity.ktx)
    api(libs.fragment.ktx)
    api(libs.bundles.immersionbar)

    api(libs.arouter)
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    kapt(libs.arouter.compiler)

    implementation(libs.startup.runtime)

    api(libs.gson)

    api(libs.bundles.moshi)
    kapt(libs.moshi.kotlin.codegen)

    api(libs.bundles.koin)
    ksp(libs.koin.compiler)

    api(libs.bundles.retrofit)

    api("androidx.browser:browser:1.3.0")

    api("com.afollestad.material-dialogs:core:3.3.0")
    api("com.afollestad.material-dialogs:bottomsheets:3.3.0")
    api("com.afollestad.material-dialogs:datetime:3.3.0")

    implementation(libs.xxpermissions)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}