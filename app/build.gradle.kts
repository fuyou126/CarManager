plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.car"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.car"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs\\Amap_2DMap_V6.0.0_20191106.jar"))
    implementation(files("libs\\AMap_Search_V9.4.0_20220808.jar"))
    implementation(files("libs\\AMap_Location_V6.4.0_20230808.jar"))
//    implementation("androidx.navigation:navigation-fragment:2.5.3")
//    implementation("androidx.navigation:navigation-ui:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("nl.joery.animatedbottombar:library:1.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.f0ris.sweetalert:library:1.6.2")
    implementation("com.github.mylhyl:Android-CircleDialog:5.3.11") // 有内存泄漏问题
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.github.gzu-liyujiang.AndroidPicker:WheelPicker:4.1.12")
    implementation("com.github.gzu-liyujiang.AndroidPicker:Common:4.1.12")
    implementation("com.github.gzu-liyujiang.AndroidPicker:WheelView:4.1.12")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.yalantis:ucrop:2.2.8-native")
    implementation("com.alibaba:fastjson:2.0.28")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:2.1.0")    //经典刷新头
//    implementation("io.github.scwang90:refresh-header-radar:2.1.0")       //雷达刷新头
//    implementation("io.github.scwang90:refresh-header-falsify:2.1.0")     //虚拟刷新头
//    implementation("io.github.scwang90:refresh-header-material:2.1.0")    //谷歌刷新头
//    implementation("io.github.scwang90:refresh-header-two-level:2.1.0")   //二级刷新头
//    implementation("io.github.scwang90:refresh-footer-ball:2.1.0")        //球脉冲加载
    implementation("io.github.scwang90:refresh-footer-classics:2.1.0")    //经典加载
}