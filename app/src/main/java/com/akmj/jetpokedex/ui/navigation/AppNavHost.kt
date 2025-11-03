package com.akmj.jetpokedex.ui.navigation

import androidx.compose.runtime.Composable
// ❗️ IMPORT BARU
import androidx.hilt.navigation.compose.hiltViewModel
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
    navController: NavHostController
    // ❗️ HAPUS 'loginViewModel' and 'startDestination' dari parameter
) {
    // ❗️ PERUBAHAN 1: Buat ViewModel di sini menggunakan Hilt
    // ViewModel ini akan di-scope ke NavHost dan dibagikan ke semua
    // screen yang memanggilnya (Login, Register, Home).
    val loginViewModel: LoginRegisterViewModel = hiltViewModel()

    // ❗️ PERUBAHAN 2: Pindahkan logic 'startDestination' ke sini.
    // Kita tanya ViewModel (yang sudah diinisialisasi dengan UseCase)
    // untuk status login saat ini.
    val startDestination = if (loginViewModel.loginState.value) {
        "home" // langsung ke home
    } else {
        "login" // ke login
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 🟩 LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel, // ❗️ Pass VM yang sudah kita buat
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
                viewModel = loginViewModel, // ❗️ Pass VM yang sama
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
            PokemonHomeScreen(navController, loginViewModel) // ❗️ Pass VM yang sama
        }

        // 🟩 LIST
        composable("pokemon_list") {
            // Screen ini akan buat VM-nya sendiri pakai hiltViewModel()
            PokemonListScreen(navController)
        }

        // 🟩 DETAIL
        composable("pokemon_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            // Screen ini juga akan buat VM-nya sendiri pakai hiltViewModel()
            PokemonDetailScreen(name)
        }
    }
}