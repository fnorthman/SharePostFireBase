plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("com.google.gms.google-services")
	id("androidx.navigation.safeargs")
	id("com.google.devtools.ksp")

}

android {
	namespace = "com.ncorp.sharepost"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.ncorp.sharepost"
		minSdk = 27
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	buildFeatures { viewBinding = true }

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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(libs.androidx.fragment)
	implementation(libs.androidx.recyclerview)
	val nav_version = "2.9.0"

	// Views/Fragments integration
	implementation("androidx.navigation:navigation-fragment:${nav_version}")
	implementation("androidx.navigation:navigation-ui:${nav_version}")

	implementation("com.google.firebase:firebase-analytics")
	// Import the Firebase BoM
	implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
	implementation("com.google.firebase:firebase-auth")
	implementation("com.google.firebase:firebase-firestore")
	implementation("com.google.firebase:firebase-storage")
	//Picasso
	implementation("com.squareup.picasso:picasso:2.8")


	implementation("com.ramotion.circlemenu:circle-menu:0.3.2")


	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)


}