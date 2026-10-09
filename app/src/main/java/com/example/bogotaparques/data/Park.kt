// Ruta: app/src/main/java/com/example/bogotaparques/data/Park.kt
package com.example.bogotaparques.data

/**
 * MODELO DE DATOS: representa un parque del Sistema Distrital de Parques (IDRD).
 * "data class" genera automáticamente equals(), hashCode(), toString() y copy().
 * Las propiedades son "val" (inmutables): Compose detecta cambios con más facilidad
 * cuando los datos no mutan por dentro.
 */
data class Park(
    val id: Int,                 // Identificador único: es lo ÚNICO que viaja por navegación
    val name: String,            // Nombre del parque
    val locality: String,        // Localidad de Bogotá (Teusaquillo, Usaquén…)
    val category: String,        // Escala IDRD: Metropolitano, Zonal, Vecinal…
    val areaHectares: Double,    // Área en hectáreas
    val latitude: Double,        // Coordenada geográfica (grados decimales)
    val longitude: Double,       // Coordenada geográfica (grados decimales)
    val schedule: String,        // Horario de atención
    val facilities: List<String>,// Equipamientos (canchas, ciclovía, lago…)
    val description: String      // Texto descriptivo
)
