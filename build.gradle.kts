// build.gradle.kts (NIVEL PROYECTO - raíz)
// Declara las versiones de los plugins. "apply false" = se declaran aquí,
// pero se APLICAN solo en el módulo que los necesite (:app).

plugins {
    id("com.android.application") version "8.7.3" apply false            // Android Gradle Plugin (AGP)
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false        // Soporte de Kotlin en Android
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false // Compilador de Compose (obligatorio desde Kotlin 2.0)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0" apply false // Genera serializadores para @Serializable
}
