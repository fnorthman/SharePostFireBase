pluginManagement {
	repositories {
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
			maven {
				url = uri("https://jitpack.io")
			}
		}
		mavenCentral()
		gradlePluginPortal()
		maven { url = uri("https://jitpack.io") }


	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
		maven { url = uri("https://jitpack.io") }  // burayı ekle
	}
}

rootProject.name = "SharePost"
include(":app")
 