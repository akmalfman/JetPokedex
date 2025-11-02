package com.akmj.jetpokedex.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun PokemonHomeScreen(
    navController: NavHostController,
    loginViewModel: LoginRegisterViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pokémon List", "Profile")

    Column {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> PokemonListScreen(navController = navController)
            1 -> UserProfileScreen(
                viewModel = loginViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

    }
}
