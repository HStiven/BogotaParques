// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/AppTopBar.kt
package com.example.bogotaparques.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * Barra superior REUTILIZABLE para todas las pantallas.
 * Reutilizar composables es una de las grandes ventajas del paradigma declarativo:
 * una función = un componente.
 *
 * @param onBack si es null, no se muestra la flecha (caso de HomeScreen).
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar aún está marcada como API experimental
@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) }, // Slot del título
        navigationIcon = {       // Slot del ícono izquierdo
            if (onBack != null) { // Renderizado CONDICIONAL: un simple if decide si existe el botón
                IconButton(onClick = onBack) { // Botón circular táctil de 48dp
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Flecha que se invierte en idiomas RTL
                        contentDescription = "Volver" // Texto para lectores de pantalla (accesibilidad)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,            // Fondo verde del tema
            titleContentColor = MaterialTheme.colorScheme.onPrimary,       // Texto que contrasta con primary
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
