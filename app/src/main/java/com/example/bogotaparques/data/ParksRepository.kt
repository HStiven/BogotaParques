// Ruta: app/src/main/java/com/example/bogotaparques/data/ParksRepository.kt
package com.example.bogotaparques.data

import android.location.Location

/**
 * REPOSITORIO con datos "quemados" (hardcoded). En una app real esto vendría
 * de una API REST o de Room; aquí basta un `object` (singleton de Kotlin:
 * existe una sola instancia en toda la app, creada la primera vez que se usa).
 *
 * Datos ilustrativos/simulados: coordenadas y áreas aproximadas.
 */
object ParksRepository {

    // Lista inmutable de parques. listOf() crea una List de solo lectura.
    val parks: List<Park> = listOf(
        Park(
            id = 1,
            name = "Parque Metropolitano Simón Bolívar",
            locality = "Teusaquillo",
            category = "Metropolitano",
            areaHectares = 113.0,
            latitude = 4.6584,
            longitude = -74.0937,
            schedule = "6:00 a.m. – 6:00 p.m.",
            facilities = listOf("Lago", "Ciclorruta", "Escenario de conciertos", "Senderos"),
            description = "El parque urbano más emblemático de la ciudad, sede de grandes festivales."
        ),
        Park(
            id = 2,
            name = "Parque El Virrey",
            locality = "Chapinero",
            category = "Zonal",
            areaHectares = 7.6,
            latitude = 4.6738,
            longitude = -74.0540,
            schedule = "Abierto 24 horas",
            facilities = listOf("Pista de trote", "Zona canina", "Gimnasio al aire libre"),
            description = "Parque lineal junto al canal, muy usado para trotar y pasear mascotas."
        ),
        Park(
            id = 3,
            name = "Parque Nacional Enrique Olaya Herrera",
            locality = "Santa Fe",
            category = "Metropolitano",
            areaHectares = 28.0,
            latitude = 4.6235,
            longitude = -74.0649,
            schedule = "6:00 a.m. – 6:00 p.m.",
            facilities = listOf("Canchas", "Senderos ecológicos", "Teatro al aire libre"),
            description = "Uno de los parques más antiguos de Bogotá, al pie de los cerros orientales."
        ),
        Park(
            id = 4,
            name = "Parque Metropolitano El Tunal",
            locality = "Tunjuelito",
            category = "Metropolitano",
            areaHectares = 54.0,
            latitude = 4.5717,
            longitude = -74.1310,
            schedule = "6:00 a.m. – 6:00 p.m.",
            facilities = listOf("Biblioteca pública", "Pista de BMX", "Canchas múltiples"),
            description = "Gran pulmón verde del sur de la ciudad, con biblioteca y escenarios deportivos."
        ),
        Park(
            id = 5,
            name = "Parque Metropolitano Timiza",
            locality = "Kennedy",
            category = "Metropolitano",
            areaHectares = 54.0,
            latitude = 4.6086,
            longitude = -74.1553,
            schedule = "6:00 a.m. – 6:00 p.m.",
            facilities = listOf("Lago", "Botes", "Zonas de picnic"),
            description = "Parque con lago central y amplias zonas verdes en el suroccidente."
        ),
        Park(
            id = 6,
            name = "Parque Usaquén",
            locality = "Usaquén",
            category = "Vecinal",
            areaHectares = 1.2,
            latitude = 4.6949,
            longitude = -74.0308,
            schedule = "Abierto 24 horas",
            facilities = listOf("Plaza fundacional", "Mercado de pulgas (domingos)"),
            description = "Corazón del barrio tradicional de Usaquén, rodeado de arquitectura colonial."
        )
    )

    /** Busca un parque por id. Devuelve null si no existe (por eso el tipo es Park?). */
    fun getParkById(id: Int): Park? = parks.find { it.id == id }

    /**
     * Calcula la distancia en KILÓMETROS entre dos coordenadas.
     * Usa Location.distanceBetween(): API NATIVA de Android que NO requiere permisos,
     * porque solo hace matemática (fórmula geodésica sobre el elipsoide WGS84);
     * no toca ningún sensor ni dato privado del usuario.
     */
    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)                                   // Arreglo donde Android escribe el resultado
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)     // results[0] = distancia en METROS
        return results[0] / 1000f                                     // Convertimos metros → kilómetros
    }

    /**
     * Devuelve el parque más cercano a (lat, lon) junto con su distancia en km.
     * minByOrNull recorre la lista y elige el elemento con el menor valor calculado.
     */
    fun nearestPark(lat: Double, lon: Double): Pair<Park, Float>? =
        parks
            .map { park -> park to distanceKm(lat, lon, park.latitude, park.longitude) } // (parque, km)
            .minByOrNull { it.second } // El par con menor distancia
}
