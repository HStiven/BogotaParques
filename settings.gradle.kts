// settings.gradle.kts (raíz del proyecto)
// Define DÓNDE busca Gradle los plugins y las librerías, y QUÉ módulos forman el proyecto.

pluginManagement {
    repositories {
        google()               // Repositorio de Google: AGP, AndroidX, Play Services
        mavenCentral()         // Repositorio general: Kotlin, kotlinx.serialization
        gradlePluginPortal()   // Portal oficial de plugins de Gradle
    }
}

dependencyResolutionManagement {
    // Obliga a declarar los repositorios solo aquí (no dentro de cada módulo)
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BogotaParques" // Nombre del proyecto
include(":app")                    // Único módulo: la aplicación
