package com.akmj.jetpokedex

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akmj.jetpokedex.viewmodel.PokemonDetailViewModel
import com.akmj.jetpokedex.viewmodel.PokemonViewModel

class PokemonViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PokemonViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                PokemonViewModel(context) as T
            }
            modelClass.isAssignableFrom(PokemonDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                PokemonDetailViewModel(context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}