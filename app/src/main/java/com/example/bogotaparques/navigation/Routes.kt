// Ruta: app/src/main/java/com/example/bogotaparques/navigation/Routes.kt
package com.example.bogotaparques.navigation

import kotlinx.serialization.Serializable

/*
 * NAVEGACIÓN TIPADA (Navigation Compose 2.8+)
 * -------------------------------------------
 * ANTES: rutas como Strings → "detail/{parkId}" y luego
 *        backStackEntry.arguments?.getString("parkId")?.toInt()
 *        Un error de tipeo ("detial/") o de tipo solo explotaba en EJECUCIÓN.
 *
 * AHORA: cada destino es una clase/objeto anotado con @Serializable.
 *        El plugin kotlinx.serialization genera el código que convierte
 *        el objeto en ruta y viceversa. El COMPILADOR verifica nombres y tipos.
 */

/** Pantalla sin parámetros → `object` (una única instancia, no necesita datos). */
@Serializable
object HomeRoute

@Serializable
object ParksListRoute

/**
 * Pantalla CON parámetro → `data class`.
 * parkId es Int: es imposible navegar pasando un String por error,
 * el código ni siquiera compilaría.
 */
@Serializable
data class DetailRoute(val parkId: Int)

@Serializable
object NearMeRoute

@Serializable
object ProfileRoute
