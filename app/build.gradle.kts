// app/build.gradle.kts (NIVEL MÓDULO :app)

plugins {
    id("com.android.application")                    // Este módulo produce un APK/AAB instalable
    id("org.jetbrains.kotlin.android")               // Compila código Kotlin para Android
    id("org.jetbrains.kotlin.plugin.compose")        // Habilita el compilador de Jetpack Compose
    id("org.jetbrains.kotlin.plugin.serialization")  // CLAVE para navegación tipada: procesa @Serializable
}

android {
    namespace = "com.example.bogotaparques"  // Paquete base para la clase R y BuildConfig
    compileSdk = 35                          // API con la que se compila (Android 15)

    defaultConfig {
        applicationId = "com.example.bogotaparques" // ID único de la app en el dispositivo / Play Store
        minSdk = 24                                 // Versión mínima soportada (Android 7.0)
        targetSdk = 35                              // Versión para la que se optimiza el comportamiento
        versionCode = 1                             // Número interno de versión (entero)
        versionName = "1.0"                         // Versión visible para el usuario
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Sin ofuscación/reducción de código (simple para clase)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Nivel de Java del código fuente
        targetCompatibility = JavaVersion.VERSION_11 // Nivel de bytecode generado
    }

    kotlinOptions {
        jvmTarget = "11" // Debe coincidir con targetCompatibility
    }

    buildFeatures {
        compose = true // Activa Jetpack Compose en este módulo
    }
}

dependencies {
    // ---------- Compose (BOM = "Bill of Materials": alinea versiones compatibles) ----------
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")                   // Núcleo de UI de Compose
    implementation("androidx.compose.ui:ui-graphics")          // Colores, formas, gráficos
    implementation("androidx.compose.ui:ui-tooling-preview")   // Anotación @Preview
    implementation("androidx.compose.material3:material3")     // Componentes Material Design 3
    debugImplementation("androidx.compose.ui:ui-tooling")      // Herramientas de preview (solo debug)

    // ---------- AndroidX base ----------
    implementation("androidx.core:core-ktx:1.15.0")                 // Extensiones Kotlin + ContextCompat
    implementation("androidx.activity:activity-compose:1.9.3")      // setContent{} y rememberLauncherForActivityResult
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")// Ciclo de vida con corrutinas

    // ---------- Navegación tipada (2.8+) ----------
    implementation("androidx.navigation:navigation-compose:2.8.5")  // NavHost, composable<T>(), toRoute()

    // ---------- Serialización (requerida por la navegación tipada) ----------
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // ---------- GPS: Fused Location Provider de Google Play Services ----------
    implementation("com.google.android.gms:play-services-location:21.3.0")
}
