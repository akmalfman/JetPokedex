package com.akmj.jetpokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akmj.jetpokedex.ui.detail.PokemonDetailScreen
import com.akmj.jetpokedex.ui.home.PokemonHomeScreen
import com.akmj.jetpokedex.ui.home.PokemonListScreen
import com.akmj.jetpokedex.ui.login.LoginScreen
import com.akmj.jetpokedex.ui.register.RegisterScreen
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginRegisterViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 🟩 LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // 🟩 REGISTER
        composable("register") {
            RegisterScreen(
                viewModel = loginViewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // 🟩 HOME
        composable("home") {
            PokemonHomeScreen(navController,loginViewModel)
        }

        // 🟩 LIST
        composable("pokemon_list") {
            PokemonListScreen(navController)
        }

        // 🟩 DETAIL
        composable("pokemon_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            PokemonDetailScreen(name)
        }
    }
}
