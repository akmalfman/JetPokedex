package com.akmj.jetpokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmj.jetpokedex.data.repository.PokemonRepository
import com.akmj.jetpokedex.domain.model.ResultsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemonList = MutableStateFlow<List<ResultsItem>>(emptyList())
    val pokemonList: StateFlow<List<ResultsItem>> = _pokemonList

    fun fetchPokemonList() {
        viewModelScope.launch {
            val result = repository.getPokemonList()
            _pokemonList.value = result
        }
    }
}
