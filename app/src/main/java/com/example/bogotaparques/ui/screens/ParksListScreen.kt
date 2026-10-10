// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/ParksListScreen.kt
package com.example.bogotaparques.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bogotaparques.data.Park
import com.example.bogotaparques.data.ParksRepository
import com.example.bogotaparques.ui.theme.BogotaParquesTheme

/**
 * VENTANA 2: Listado de parques.
 * @param onParkClick devuelve el id (Int) del parque tocado; la navegación la decide AppNavigation.
 */
@Composable
fun ParksListScreen(onParkClick: (Int) -> Unit, onBack: () -> Unit) {
    val parks = ParksRepository.parks // Lee los datos simulados

    Scaffold(topBar = { AppTopBar(title = "Parques de Bogotá", onBack = onBack) }) { innerPadding ->

        // LazyColumn: lista VERTICAL "perezosa". Solo compone los ítems VISIBLES en pantalla
        // (equivale a RecyclerView, pero sin Adapter ni ViewHolder).
        // Una Column normal compondría TODOS los ítems aunque no se vean → ineficiente.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),            // Margen alrededor de TODA la lista (también al hacer scroll)
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre ítems
        ) {
            // items(lista, key): genera un ítem por elemento.
            // key = id estable → Compose sabe qué ítem es cuál si la lista cambia (mejor rendimiento y animaciones).
            items(items = parks, key = { it.id }) { park ->
                ParkItem(park = park, onClick = { onParkClick(park.id) })
            }
        }
    }
}

/** Fila de la lista que representa un parque. */
@Composable
private fun ParkItem(park: Park, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(park.name, style = MaterialTheme.typography.titleMedium)       // Nombre en estilo título
            Text("Localidad: ${park.locality}", style = MaterialTheme.typography.bodyMedium) // Interpolación de strings
            // AssistChip: "pastilla" informativa de Material 3
            AssistChip(onClick = onClick, label = { Text(park.category) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParksListScreenPreview() {
    BogotaParquesTheme {
        ParksListScreen(
            onParkClick = {},
            onBack = {}
        )
    }
}
