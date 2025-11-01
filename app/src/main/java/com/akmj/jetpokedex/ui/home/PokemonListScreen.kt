package com.akmj.jetpokedex.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akmj.jetpokedex.ui.viewmodel.PokemonViewModel

@Composable
fun PokemonListScreen(
    navController: NavHostController,
    viewModel: PokemonViewModel = viewModel()
) {
    val pokemonList by viewModel.pokemonList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pokémon List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(), // pastikan penuh
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pokemonList.filter { true }) { pokemon ->
                // sekarang pokemon pasti non-null
                PokemonItem(
                    name = pokemon.name ?: "Unknown",
                    onClick = { name ->
                        navController.navigate("pokemon_detail/$name")
                    }
                )
            }

        }

    }
}

@Composable
fun PokemonItem(
    name: String,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(name) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // ✨ warna latar Card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = name.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}