package com.akmj.jetpokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akmj.jetpokedex.ui.detail.PokemonDetailScreen
import com.akmj.jetpokedex.ui.home.PokemonHomeScreen
import com.akmj.jetpokedex.ui.home.PokemonListScreen
import com.akmj.jetpokedex.ui.home.UserProfileScreen
import com.akmj.jetpokedex.ui.login.LoginScreen
import com.akmj.jetpokedex.ui.register.RegisterScreen
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun AppNavHost(navController: NavHostController, loginViewModel: LoginRegisterViewModel) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = loginViewModel,
                onRegisterSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        composable("home") {
            PokemonHomeScreen(navController, loginViewModel)
        }

        composable("pokemon_list") {
            PokemonListScreen(navController)
        }

        composable("pokemon_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            PokemonDetailScreen(name)
        }

        composable("profile") {
            UserProfileScreen(loginViewModel)
        }
    }
}
