// Ruta: app/src/main/java/com/example/bogotaparques/ui/screens/NearMeScreen.kt
package com.example.bogotaparques.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.bogotaparques.data.ParksRepository
import com.example.bogotaparques.ui.theme.BogotaParquesTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/** Posición del usuario. Inmutable: cada lectura del GPS crea una NUEVA instancia → Compose detecta el cambio. */
private data class UserPosition(val latitude: Double, val longitude: Double, val accuracyMeters: Float)

// Permisos que pediremos juntos (Android 12+ exige ofrecer FINE y COARSE a la vez).
private val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

/**
 * VENTANA 4: "Cerca de mí".
 *
 * Puente HARDWARE ↔ UI en el paradigma DECLARATIVO:
 *   GPS (hardware) → FusedLocationProviderClient → callback → ESTADO (mutableStateOf)
 *   → Compose detecta el cambio → RECOMPONE la UI con la nueva distancia.
 * Nunca escribimos "textView.setText(...)": solo cambiamos el estado y la UI se actualiza sola.
 */
@Composable
fun NearMeScreen(onBack: () -> Unit, onOpenPark: (Int) -> Unit) {
    // LocalContext.current: obtiene el Context de Android desde dentro de un composable.
    val context = LocalContext.current

    // ---------- 1. ESTADO: ¿tenemos permiso? ----------
    // Valor inicial = consulta REAL al sistema (puede que el usuario ya lo haya concedido antes).
    var hasPermission by remember {
        mutableStateOf(
            LOCATION_PERMISSIONS.any { permission ->
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        )
    }
    var permissionDenied by remember { mutableStateOf(false) } // Para mostrar mensaje si el usuario dice "No"

    // ---------- 2. ESTADO: posición del usuario ----------
    var userPosition by remember { mutableStateOf<UserPosition?>(null) } // null = aún sin lectura
    var updatesCount by remember { mutableIntStateOf(0) }                // Cuántas lecturas han llegado (demo "tiempo real")

    // ---------- 3. LAUNCHER DE PERMISOS EN TIEMPO DE EJECUCIÓN ----------
    // rememberLauncherForActivityResult: registra un "contrato" con el sistema.
    //  - RequestMultiplePermissions(): contrato que muestra el diálogo nativo de permisos.
    //  - La lambda es el CALLBACK: se ejecuta cuando el usuario responde.
    //    `result` es un Map<String, Boolean> (permiso → concedido sí/no).
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.any { it } // Basta con FINE o COARSE
        hasPermission = granted                // ← Cambiar el estado dispara la recomposición
        permissionDenied = !granted
    }

    // ---------- 4. LaunchedEffect: pedir el permiso al ENTRAR a la pantalla ----------
    // LaunchedEffect(key): ejecuta un bloque (corrutina) cuando el composable entra en la
    // composición, y lo RE-ejecuta solo si la `key` cambia. Con key = Unit corre UNA sola vez.
    // Es el lugar correcto para "efectos secundarios": NUNCA lances el diálogo directamente
    // en el cuerpo del composable, porque el cuerpo se re-ejecuta en cada recomposición.
    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(LOCATION_PERMISSIONS) // Muestra el diálogo del sistema
    }

    // ---------- 5. CLIENTE DE UBICACIÓN (Google Play Services) ----------
    // FusedLocationProviderClient "fusiona" GPS + Wi-Fi + red celular + sensores para dar
    // la mejor ubicación con el menor gasto de batería. remember: se crea una sola vez.
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // ---------- 6. DisposableEffect: escuchar el GPS en TIEMPO REAL ----------
    // Como LaunchedEffect, pero con un bloque onDispose{} OBLIGATORIO para LIMPIAR recursos.
    // key = hasPermission: cuando el permiso pasa de false → true, el efecto se reinicia
    // y empieza a escuchar. Al salir de la pantalla, onDispose apaga el GPS (ahorra batería).
    DisposableEffect(hasPermission) {
        // Callback que el sistema invoca cada vez que hay una nueva ubicación
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    // ← AQUÍ el hardware toca la UI: solo asignamos un nuevo estado.
                    userPosition = UserPosition(location.latitude, location.longitude, location.accuracy)
                    updatesCount++
                }
            }
        }

        if (hasPermission) {
            startLocationUpdates(fusedClient, callback) // Ver función abajo
        }

        onDispose {
            // Se ejecuta al salir de la pantalla o si cambia la key: dejamos de escuchar el GPS.
            fusedClient.removeLocationUpdates(callback)
        }
    }

    // ---------- 7. ESTADO DERIVADO: parque más cercano ----------
    // remember(userPosition): recalcula SOLO cuando cambia la posición.
    val nearest = remember(userPosition) {
        userPosition?.let { ParksRepository.nearestPark(it.latitude, it.longitude) }
    }

    // ---------- 8. UI DECLARATIVA: describe QUÉ mostrar según el estado ----------
    Scaffold(topBar = { AppTopBar(title = "Cerca de mí", onBack = onBack) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // `when` sin argumento: elige UNA rama según el estado actual (máquina de estados de la UI)
            when {
                // Caso A: sin permiso
                !hasPermission -> {
                    Text(
                        text = if (permissionDenied)
                            "Sin permiso de ubicación no podemos calcular la distancia."
                        else
                            "Necesitamos tu ubicación para encontrar el parque más cercano.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = { permissionLauncher.launch(LOCATION_PERMISSIONS) }) {
                        Text("Conceder permiso")
                    }
                }

                // Caso B: con permiso pero esperando la primera lectura del GPS
                userPosition == null -> {
                    CircularProgressIndicator() // Rueda de carga indeterminada
                    Text("Buscando señal GPS…")
                }

                // Caso C: tenemos posición → mostramos resultados
                else -> {
                    val pos = userPosition!! // Seguro: la rama anterior descartó null

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Tu posición", style = MaterialTheme.typography.titleMedium)
                            Text("Lat: %.5f  Lon: %.5f".format(pos.latitude, pos.longitude))
                            Text("Precisión: ±%.0f m".format(pos.accuracyMeters))
                            Text("Lecturas recibidas: $updatesCount") // Sube en vivo al moverte
                        }
                    }

                    nearest?.let { (park, km) -> // Desestructuración del Pair(parque, distancia)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Parque más cercano", style = MaterialTheme.typography.titleMedium)
                                Text(park.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                Text("A %.2f km de ti".format(km), style = MaterialTheme.typography.bodyLarge)
                                OutlinedButton(onClick = { onOpenPark(park.id) }) { // Navegación tipada con el id
                                    Text("Ver detalle")
                                }
                            }
                        }
                    }

                    // Distancia a TODOS los parques, ordenada de menor a mayor
                    Text("Todos los parques", style = MaterialTheme.typography.titleMedium)
                    ParksRepository.parks
                        .map { it to ParksRepository.distanceKm(pos.latitude, pos.longitude, it.latitude, it.longitude) }
                        .sortedBy { it.second }
                        .forEach { (park, km) ->
                            Text("${park.name}: %.2f km".format(km), modifier = Modifier.fillMaxWidth())
                        }
                }
            }
        }
    }
}

/**
 * Configura y arranca las actualizaciones de ubicación.
 * @SuppressLint("MissingPermission"): le decimos al linter que SÍ verificamos el permiso
 * antes de llamar esta función (en el DisposableEffect con `if (hasPermission)`).
 */
@SuppressLint("MissingPermission")
private fun startLocationUpdates(
    client: com.google.android.gms.location.FusedLocationProviderClient,
    callback: LocationCallback
) {
    // LocationRequest.Builder(prioridad, intervaloMs)
    val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L) // Alta precisión (GPS), cada 5 s
        .setMinUpdateIntervalMillis(2_000L)  // Nunca más rápido que cada 2 s
        .setMinUpdateDistanceMeters(5f)      // Solo notifica si te moviste al menos 5 m
        .build()

    // Looper.getMainLooper(): el callback se ejecuta en el hilo PRINCIPAL (UI),
    // que es donde se puede modificar el estado de Compose sin problemas.
    client.requestLocationUpdates(request, callback, Looper.getMainLooper())

    // Bonus: la última ubicación conocida suele llegar al instante (evita esperar el primer "fix").
    client.lastLocation.addOnSuccessListener { location ->
        location?.let { callback.onLocationResult(LocationResult.create(listOf(it))) }
    }
}

@Preview(showBackground = true)
@Composable
private fun NearMeScreenPreview() {
    BogotaParquesTheme {
        NearMeScreen(
            onBack = {},
            onOpenPark = {}
        )
    }
}
