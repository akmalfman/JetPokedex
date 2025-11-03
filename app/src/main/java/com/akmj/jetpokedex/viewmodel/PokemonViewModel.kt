package com.akmj.jetpokedex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmj.jetpokedex.data.repository.PokemonRepository
import com.akmj.jetpokedex.domain.model.ResultsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(context: Context) : ViewModel() {
    private val repository = PokemonRepository(context)

    private val _pokemonList = MutableStateFlow<List<ResultsItem>>(emptyList())
    val pokemonList: StateFlow<List<ResultsItem>> = _pokemonList

    private var currentPage = 0
    private val pageSize = 10

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchPokemonList() {
        if (_isLoading.value) return
        _isLoading.value = true
        _errorMessage.value = null

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
                    _isOfflineMode.value = false
                } else if (_pokemonList.value.isEmpty()) {
                    _errorMessage.value = "Tidak ada data tersedia"
                    _isOfflineMode.value = true
                }
            } catch (e: Exception) {
                _errorMessage.value = "Mode Offline - Menampilkan data tersimpan"
                _isOfflineMode.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Jika mode offline dan ada query, cari dari local
        if (_isOfflineMode.value && query.isNotEmpty()) {
            viewModelScope.launch {
                val searchResults = repository.searchPokemon(query)
                _pokemonList.value = searchResults
            }
        }
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

    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshData()
            _pokemonList.value = emptyList()
            currentPage = 0
            fetchPokemonList()
        }
    }
}