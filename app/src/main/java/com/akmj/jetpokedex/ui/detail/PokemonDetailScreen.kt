package com.akmj.jetpokedex.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// ❗️ HAPUS: import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// ❗️ IMPORT BARU: Hilt
import androidx.hilt.navigation.compose.hiltViewModel
import com.akmj.jetpokedex.viewmodel.PokemonDetailViewModel
// ❗️ HAPUS: import com.akmj.jetpokedex.viewmodel.PokemonViewModelFactory

@Composable
fun PokemonDetailScreen(
    name: String?,
    // ❗️ PERUBAHAN 1: Ganti factory dengan hiltViewModel()
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    // ❗️ PERUBAHAN 2: Observe 'pokemonDetail' (objek tunggal)
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        if (name != null) {
            // ❗️ PERUBAHAN 3: Panggil fungsi ViewModel yang baru
            viewModel.fetchPokemonDetail(name)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            // ❗️ Tampilkan nama dari state jika ada, jika tidak, pakai nav argument
            text = (pokemonDetail?.name ?: name)?.replaceFirstChar { it.uppercase() } ?: "Unknown Pokémon",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 🔔 Offline Banner (Tidak berubah, logic masih valid)
        if (isOfflineMode) {
            Card(
                // ... (tidak ada perubahan) ...
            ) {
                Row(
                    // ... (tidak ada perubahan) ...
                ) {
                    // ... (tidak ada perubahan) ...
                    Text(
                        errorMessage ?: "Mode Offline",
                        // ... (tidak ada perubahan) ...
                    )
                }
            }
        }

        // 🔄 Loading State
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            // ❗️ PERUBAHAN 4: Cek 'pokemonDetail' (bukan 'abilityList.isEmpty()')
        } else if (pokemonDetail == null) { // Gagal load ATAU belum load
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (errorMessage != null) "Gagal memuat detail" else "Tidak ada data",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // --- ❗️ Bagian Sukses (pokemonDetail != null) ---

            Text(
                text = "Abilities:",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ❗️ PERUBAHAN 5: Iterasi 'pokemonDetail.abilities'
                items(pokemonDetail!!.abilities) { ability ->
                    // ❗️ PERUBAHAN 6: Akses data domain (non-null)
                    val abilityName = ability.name
                    val isHidden = ability.isHidden

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isHidden)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = abilityName.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            if (isHidden) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Hidden Ability",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}