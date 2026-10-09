// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/HomeScreen.kt
package com.example.bogotaparques.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * VENTANA 1: Menú principal.
 * Recibe LAMBDAS en vez del navController: la pantalla no sabe CÓMO se navega,
 * solo avisa "el usuario tocó X". Esto se llama "state hoisting / event up":
 * los eventos suben, los datos bajan. Hace la pantalla reutilizable y testeable.
 */
@Composable
fun HomeScreen(
    onParksClick: () -> Unit,
    onNearMeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Scaffold: esqueleto estándar de Material (topBar, bottomBar, FAB, contenido).
    Scaffold(
        topBar = { AppTopBar(title = "Bogotá Parques") } // Sin onBack: es la pantalla raíz
    ) { innerPadding -> // Espacio que ocupan las barras; hay que respetarlo para no tapar contenido

        // Column: apila hijos VERTICALMENTE (equivale a LinearLayout vertical).
        Column(
            modifier = Modifier
                .fillMaxSize()           // Ocupa todo el ancho y alto disponible
                .padding(innerPadding)   // Primero respeta el espacio de la TopAppBar…
                .padding(24.dp),         // …y luego agrega margen interno de 24dp. ¡El ORDEN de los modificadores importa!
            horizontalAlignment = Alignment.CenterHorizontally, // Centra los hijos en el eje X
            verticalArrangement = Arrangement.spacedBy(16.dp)   // 16dp de separación entre cada hijo
        ) {
            Icon(
                imageVector = Icons.Default.Place,          // Ícono vectorial incluido en Material
                contentDescription = null,                  // null = decorativo, el lector de pantalla lo ignora
                modifier = Modifier.size(72.dp),            // Tamaño fijo cuadrado
                tint = MaterialTheme.colorScheme.primary    // Color tomado del tema (no "quemado")
            )
            Text(
                text = "Bienvenido, explorador",
                style = MaterialTheme.typography.headlineMedium, // Estilo tipográfico de Material 3
                fontWeight = FontWeight.Bold                     // Negrita
            )
            Text(
                text = "Descubre los parques del Sistema Distrital de Parques de Bogotá.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center // Centra el texto cuando ocupa varias líneas
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio vacío extra (separador invisible)

            // Accesos rápidos: el mismo composable reutilizado 3 veces con distintos datos
            MenuCard("Listado de parques", "Explora todos los parques", Icons.Default.Place, onParksClick)
            MenuCard("Cerca de mí", "Usa tu GPS para encontrar el más cercano", Icons.Default.LocationOn, onNearMeClick)
            MenuCard("Mi perfil", "Tus datos de explorador", Icons.Default.Person, onProfileClick)
        }
    }
}

/**
 * Tarjeta de menú reutilizable. "private" porque solo se usa en este archivo.
 */
@Composable
private fun MenuCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,                              // Card clickeable con efecto "ripple"
        modifier = Modifier.fillMaxWidth(),             // Ocupa todo el ancho de la Column
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra
    ) {
        // Row: organiza hijos HORIZONTALMENTE.
        Row(
            modifier = Modifier.padding(16.dp),               // Margen interno de la tarjeta
            verticalAlignment = Alignment.CenterVertically    // Centra ícono y textos en el eje Y
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))          // Separación horizontal
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
