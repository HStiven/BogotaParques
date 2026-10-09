// Ruta: app/src/main/java/com/example/bogotaparques/navigation/AppNavigation.kt
package com.example.bogotaparques.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bogotaparques.ui.screens.DetailScreen
import com.example.bogotaparques.ui.screens.HomeScreen
import com.example.bogotaparques.ui.screens.NearMeScreen
import com.example.bogotaparques.ui.screens.ParksListScreen
import com.example.bogotaparques.ui.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {

        composable<HomeRoute> {
            HomeScreen(
                onParksClick = { navController.navigate(ParksListRoute) },
                onNearMeClick = { navController.navigate(NearMeRoute) },
                onProfileClick = { navController.navigate(ProfileRoute) }
            )
        }

        composable<ParksListRoute> {
            ParksListScreen(
                onParkClick = { id -> navController.navigate(DetailRoute(parkId = id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable<DetailRoute> { backStackEntry ->
            val route: DetailRoute = backStackEntry.toRoute()
            DetailScreen(
                parkId = route.parkId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<NearMeRoute> {
            NearMeScreen(
                onBack = { navController.popBackStack() },
                // Desde "Cerca de mí" se puede abrir el detalle del parque más cercano
                onOpenPark = { id -> navController.navigate(DetailRoute(parkId = id)) }
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
    }
}
