package com.akmj.jetpokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akmj.jetpokedex.ui.detail.PokemonDetailScreen
import com.akmj.jetpokedex.ui.home.PokemonHomeScreen
import com.akmj.jetpokedex.ui.home.PokemonListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            PokemonHomeScreen(navController)
        }
        composable("pokemon_list") {
            PokemonListScreen(navController)
        }
        composable("pokemon_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            PokemonDetailScreen(name)
        }
    }
}
