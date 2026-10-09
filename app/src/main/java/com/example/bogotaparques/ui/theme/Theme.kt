// Ruta: app/src/main/java/com/example/bogotaparques/ui/theme/Theme.kt
package com.example.bogotaparques.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema de colores claro: asigna nuestros colores a los "roles" de Material 3.
private val LightColors = lightColorScheme(
    primary = ParkGreen,                 // Botones, TopAppBar, elementos principales
    primaryContainer = ParkGreenLight,   // Fondos de tarjetas destacadas
    secondary = BogotaYellow,            // Acentos
    error = BogotaRed                    // Mensajes de error
)

// Esquema oscuro: colores más claros para contrastar sobre fondo negro.
private val DarkColors = darkColorScheme(
    primary = ParkGreenDark,
    secondary = BogotaYellow,
    error = BogotaRed
)

/**
 * Tema de la app. Envuelve TODO el contenido: cualquier composable dentro puede
 * leer MaterialTheme.colorScheme / MaterialTheme.typography.
 *
 * @param darkTheme por defecto sigue la configuración del sistema.
 * @param content lambda composable = "slot" donde va el contenido hijo.
 */
@Composable
fun BogotaParquesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Lee si el teléfono está en modo oscuro
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors, // Elige el esquema
        content = content                                         // Dibuja los hijos
    )
}
