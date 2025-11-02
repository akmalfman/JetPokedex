package com.akmj.jetpokedex.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.akmj.jetpokedex.viewmodel.PokemonViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PokemonListScreen(
    navController: NavHostController,
    viewModel: PokemonViewModel = viewModel()
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()

    // Fetch awal
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList()
    }

    // Pagination handler
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleItemIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 3 && !viewModel.isLoading.value) {
                    viewModel.fetchPokemonList()
                }
            }
    }

    // List hasil filter (tidak mempengaruhi data utama)
    val filteredList = remember(pokemonList, query) {
        if (query.isBlank()) pokemonList
        else pokemonList.filter { it.name?.contains(query, ignoreCase = true) == true }
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

        // 🔍 Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = { Text("Cari Pokémon...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        // 🔹 List Pokémon
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { pokemon ->
                PokemonItem(
                    name = pokemon.name ?: "Unknown",
                    onClick = { name ->
                        navController.navigate("pokemon_detail/$name")
                    }
                )
            }

            // 🔄 Loading indicator di bawah list
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
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
            .clickable { onClick(name) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
