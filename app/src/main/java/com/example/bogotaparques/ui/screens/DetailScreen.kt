// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/DetailScreen.kt
package com.example.bogotaparques.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bogotaparques.data.ParksRepository

/**
 * VENTANA 3: Detalle del parque.
 * Recibe SOLO el id (Int) gracias a la navegación tipada (DetailRoute(parkId)).
 * Buena práctica: navegar con identificadores, no con objetos completos;
 * la pantalla destino busca los datos en la fuente de verdad (el repositorio).
 */
@Composable
fun DetailScreen(parkId: Int, onBack: () -> Unit) {
    // remember(parkId): la búsqueda se hace UNA vez y se cachea; solo se repite si parkId cambia.
    val park = remember(parkId) { ParksRepository.getParkById(parkId) }

    Scaffold(topBar = { AppTopBar(title = park?.name ?: "Detalle", onBack = onBack) }) { innerPadding ->

        // Caso borde: id inexistente → mostramos un mensaje en vez de fallar.
        if (park == null) {
            // Box: apila hijos uno encima de otro; aquí lo usamos para centrar un único texto.
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Parque no encontrado")
            }
            return@Scaffold // Sale de la lambda del Scaffold (no de la función entera)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Permite desplazarse si el contenido no cabe.
                // rememberScrollState() recuerda la posición del scroll entre recomposiciones.
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(park.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(park.description, style = MaterialTheme.typography.bodyLarge)

            // Ficha técnica en una tarjeta con color de contenedor del tema
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Ficha técnica", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider() // Línea separadora horizontal
                    InfoRow("ID", park.id.toString())
                    InfoRow("Localidad", park.locality)
                    InfoRow("Escala IDRD", park.category)
                    InfoRow("Área", "${park.areaHectares} ha")
                    InfoRow("Horario", park.schedule)
                    InfoRow("Coordenadas", "%.4f, %.4f".format(park.latitude, park.longitude)) // 4 decimales
                }
            }

            Text("Equipamientos", style = MaterialTheme.typography.titleMedium)
            // forEach dentro de un composable: genera un Text por cada elemento de la lista
            park.facilities.forEach { facility -> Text("• $facility") }
        }
    }
}

/** Fila "etiqueta: valor" reutilizable. */
@Composable
private fun InfoRow(label: String, value: String) {
    Text("$label: $value", style = MaterialTheme.typography.bodyMedium)
}
