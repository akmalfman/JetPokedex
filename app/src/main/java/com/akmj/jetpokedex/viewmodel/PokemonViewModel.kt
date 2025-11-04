package com.akmj.jetpokedex.viewmodel

import com.akmj.jetpokedex.domain.model.PokemonEntry
import com.akmj.jetpokedex.domain.usecase.GetPokemonListUseCase
import com.akmj.jetpokedex.domain.usecase.RefreshDataUseCase
import com.akmj.jetpokedex.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonEntry>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntry>> = _pokemonList

    private var currentPage = 0
    private val pageSize = 10
    private var canLoadMore = true

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchPokemonList() {
        if (_isLoading.value || !canLoadMore) return
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val resultPage = getPokemonListUseCase(
                    offset = currentPage * pageSize,
                    limit = pageSize
                )

                _pokemonList.value = (_pokemonList.value + resultPage.pokemonList)
                    .distinctBy { it.name }

                currentPage++
                canLoadMore = resultPage.next != null
                _isOfflineMode.value = false

            } catch (e: Exception) {
                _errorMessage.value = "Mode Offline - Menampilkan data tersimpan"
                _isOfflineMode.value = true
                canLoadMore = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        if (_isOfflineMode.value && query.isNotEmpty()) {
            viewModelScope.launch {
                val searchResults = searchPokemonUseCase(query)
                _pokemonList.value = searchResults
            }
        }
    }
}