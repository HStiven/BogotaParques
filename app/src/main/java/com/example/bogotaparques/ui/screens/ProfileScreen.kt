// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/ProfileScreen.kt
package com.example.bogotaparques.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.bogotaparques.data.ParksRepository

/**
 * VENTANA 5: Perfil básico del explorador.
 * Ejemplo sencillo de ESTADO LOCAL: editar el nombre y marcar parques visitados.
 */
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    // rememberSaveable: como remember, pero además SOBREVIVE a la rotación de pantalla
    // (guarda el valor en el Bundle de la Activity).
    // "by" (delegado): permite escribir `name` en vez de `name.value`.
    var name by rememberSaveable { mutableStateOf("Explorador Bogotano") }
    var visited by rememberSaveable { mutableIntStateOf(2) } // mutableIntStateOf: versión optimizada para Int (sin "boxing")
    var notifications by rememberSaveable { mutableStateOf(true) }

    Scaffold(topBar = { AppTopBar(title = "Mi perfil", onBack = onBack) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar circular con la inicial del nombre
            Box(
                modifier = Modifier
                    .size(96.dp)                                    // Cuadrado de 96dp…
                    .clip(CircleShape)                              // …recortado en círculo…
                    .background(MaterialTheme.colorScheme.primary), // …y pintado de verde
                contentAlignment = Alignment.Center                 // Centra la letra dentro
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "?", // Primera letra (o "?" si está vacío)
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Campo de texto CONTROLADO: muestra `name` y, cuando el usuario escribe,
            // onValueChange actualiza el estado → recomposición → el avatar cambia de letra solo.
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,                  // No permite saltos de línea
                modifier = Modifier.fillMaxWidth()
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Parques visitados: $visited de ${ParksRepository.parks.size}")
                    Text("Nivel: ${if (visited >= 4) "Guardabosques" else "Caminante"}") // Expresión if en línea
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Notificaciones", modifier = Modifier.weight(1f)) // weight(1f): ocupa el espacio sobrante
                        Switch(checked = notifications, onCheckedChange = { notifications = it })
                    }
                }
            }

            Button(
                onClick = { if (visited < ParksRepository.parks.size) visited++ }, // Cambia el estado → la UI se redibuja
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar visita")
            }
        }
    }
}
