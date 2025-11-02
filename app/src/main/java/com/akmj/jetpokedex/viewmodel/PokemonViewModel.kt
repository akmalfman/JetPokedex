package com.akmj.jetpokedex.viewmodel

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

    private var currentPage = 0
    private val pageSize = 10

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPokemonList() {
        if (_isLoading.value) return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = repository.getPokemonList(
                    offset = currentPage * pageSize,
                    limit = pageSize
                )
                if (result.isNotEmpty()) {
                    _pokemonList.value = (_pokemonList.value + result)
                        .distinctBy { it.name }
                    currentPage++
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredList(): List<ResultsItem> {
        val query = _searchQuery.value.lowercase()
        return if (query.isBlank()) {
            _pokemonList.value
        } else {
            _pokemonList.value.filter {
                it.name?.contains(query, ignoreCase = true) == true
            }
        }
    }
}
