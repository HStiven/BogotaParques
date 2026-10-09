// Ruta: app/src/main/java/com/example/bogotaparques/MainActivity.kt
package com.example.bogotaparques

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bogotaparques.navigation.AppNavigation
import com.example.bogotaparques.ui.theme.BogotaParquesTheme

/**
 * ÚNICA Activity de la app (arquitectura "Single Activity").
 * Las 5 "ventanas" NO son Activities: son funciones @Composable que el NavHost
 * intercambia dentro de esta misma Activity.
 */
class MainActivity : ComponentActivity() { // ComponentActivity: base mínima compatible con Compose

    override fun onCreate(savedInstanceState: Bundle?) { // Se llama al crear la pantalla
        super.onCreate(savedInstanceState)               // Inicialización obligatoria del padre
        enableEdgeToEdge()                               // Dibuja detrás de barras de estado/navegación

        // setContent { }: reemplaza el antiguo setContentView(R.layout.xxx).
        // A partir de aquí la UI se DECLARA con funciones Kotlin, no con XML.
        setContent {
            BogotaParquesTheme {   // Aplica colores Material 3 a todo el árbol
                AppNavigation()    // Raíz de la UI: el grafo de navegación
            }
        }
    }
}
