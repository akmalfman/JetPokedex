package com.akmj.jetpokedex.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akmj.jetpokedex.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    name: String?,
    viewModel: PokemonDetailViewModel = viewModel()
) {

    val abilityList by viewModel.abilityList.collectAsState()
    LaunchedEffect(Unit) {
        if (name != null) {
            viewModel.fetchAbilityList(name)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = name ?: "Unknown Pokémon",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Info Pokémon
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(), // pastikan penuh
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(abilityList.filter { true }) { ability ->
                val abilityName = ability.ability?.name ?: "Unknown Ability"

                Text(
                    text = "• $abilityName",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

        }
    }
}
