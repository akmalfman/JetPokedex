package com.akmj.jetpokedex.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// ❗️ HAPUS: import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// ❗️ UBAH IMPORT: Kita tidak pakai factory lagi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.akmj.jetpokedex.viewmodel.PokemonViewModel
// ❗️ HAPUS: import com.akmj.jetpokedex.viewmodel.PokemonViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavHostController,
    // ❗️ PERUBAHAN 1: Hilt akan 'inject' ViewModel secara otomatis
    viewModel: PokemonViewModel = hiltViewModel()
) {
    // Semua 'collectAsState' ini sudah benar,
    // karena 'pokemonList' sekarang adalah StateFlow<List<PokemonEntry>>
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val listState = rememberLazyListState()

    // Fetch awal (Ini masih benar)
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList()
    }

    // Pagination handler (Ini masih benar)
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleItemIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisibleItemIndex != null &&
                    lastVisibleItemIndex >= totalItems - 3 &&
                    !viewModel.isLoading.value &&
                    !isOfflineMode) {
                    viewModel.fetchPokemonList()
                }
            }
    }

    // List hasil filter
    val filteredList = remember(pokemonList, query) {
        if (query.isBlank()) pokemonList
        // ❗️ PERUBAHAN 2: Tipe data 'it.name' sekarang non-null String
        else pokemonList.filter { it.name.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Pokémon List")
                        if (isOfflineMode) {
                            Text(
                                "Mode Offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔔 Offline Banner (Ini masih benar)
            if (isOfflineMode) {
                Card(
                    // ... (tidak ada perubahan di sini) ...
                ) {
                    Row(
                        // ... (tidak ada perubahan di sini) ...
                    ) {
                        // ... (tidak ada perubahan di sini) ...
                        Text(
                            errorMessage ?: "Mode Offline",
                            // ... (tidak ada perubahan di sini) ...
                        )
                    }
                }
            }

            // 🔍 Search Bar (Ini masih benar)
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Cari Pokémon...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )

            // 🔹 List Pokémon
            if (filteredList.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ❗️ PERUBAHAN 3: 'pokemon.name' sekarang non-null
                    items(filteredList) { pokemon ->
                        PokemonItem(
                            name = pokemon.name, // Hapus '?: "Unknown"'
                            onClick = { name ->
                                // Navigasi by 'name' masih benar
                                navController.navigate("pokemon_detail/$name")
                            }
                        )
                    }

                    // 🔄 Loading indicator di bawah list (Ini masih benar)
                    if (isLoading) {
                        item {
                            Box(
                                // ... (tidak ada perubahan di sini) ...
                            ) {
                                CircularProgressIndicator()
                            }
                        }
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