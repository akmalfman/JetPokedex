package com.akmj.jetpokedex.viewmodel

// ❗️ HAPUS: import android.content.Context
// ❗️ HAPUS: import com.akmj.jetpokedex.data.repository.PokemonRepositoryImpl
// ❗️ HAPUS: import com.akmj.jetpokedex.data.remote.response.ResultsItem

// ❗️ IMPORT BARU: Entitas Domain Murni
import com.akmj.jetpokedex.domain.model.PokemonEntry
// ❗️ IMPORT BARU: Use Cases
import com.akmj.jetpokedex.domain.usecase.GetPokemonListUseCase
import com.akmj.jetpokedex.domain.usecase.RefreshDataUseCase
import com.akmj.jetpokedex.domain.usecase.SearchPokemonUseCase
// ❗️ IMPORT BARU: Hilt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ❗️ ANOTASI BARU: Memberi tahu Hilt ini adalah ViewModel
@HiltViewModel
// ❗️ CONSTRUCTOR BARU: Hilt akan 'menyuntikkan' Use Cases secara otomatis
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val refreshDataUseCase: RefreshDataUseCase
) : ViewModel() {

    // ❗️ HAPUS: val repository = PokemonRepositoryImpl(context)

    // ❗️ GANTI TIPE DATA: Dari DTO (ResultsItem) ke Entitas Domain (PokemonEntry)
    private val _pokemonList = MutableStateFlow<List<PokemonEntry>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntry>> = _pokemonList

    private var currentPage = 0
    private val pageSize = 10
    // ❗️ STATE BARU: Untuk tahu kapan harus berhenti 'load more'
    private var canLoadMore = true

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Kita tetap simpan isOfflineMode untuk logic UI Anda
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchPokemonList() {
        // ❗️ Jangan load lagi jika sedang loading atau sudah mentok
        if (_isLoading.value || !canLoadMore) return
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // ❗️ PANGGIL USE CASE: Bukan repository
                // Use case ini sudah punya logic offline-first di dalamnya
                val resultPage = getPokemonListUseCase(
                    offset = currentPage * pageSize,
                    limit = pageSize
                )

                _pokemonList.value = (_pokemonList.value + resultPage.pokemonList)
                    .distinctBy { it.name }

                currentPage++
                // ❗️ Kita dapat info 'next' dari Use Case
                canLoadMore = resultPage.next != null
                _isOfflineMode.value = false

            } catch (e: Exception) {
                _errorMessage.value = "Mode Offline - Menampilkan data tersimpan"
                _isOfflineMode.value = true
                canLoadMore = false // Berhenti paginasi jika gagal
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Logic ini masih valid, tapi sekarang kita panggil Use Case
        if (_isOfflineMode.value && query.isNotEmpty()) {
            viewModelScope.launch {
                // ❗️ PANGGIL USE CASE
                val searchResults = searchPokemonUseCase(query)
                // ❗️ Tipe datanya sudah pas (List<PokemonEntry>)
                _pokemonList.value = searchResults
            }
        }
    }

    // ❗️ GANTI TIPE DATA: Return List<PokemonEntry>
    fun getFilteredList(): List<PokemonEntry> {
        val query = _searchQuery.value.lowercase()
        return if (query.isBlank()) {
            _pokemonList.value
        } else {
            _pokemonList.value.filter {
                // ❗️ Properti 'name' ada di PokemonEntry, jadi ini aman
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            // ❗️ PANGGIL USE CASE
            refreshDataUseCase()

            // Reset state
            _pokemonList.value = emptyList()
            currentPage = 0
            canLoadMore = true
            _errorMessage.value = null

            fetchPokemonList() // Ambil data halaman pertama
        }
    }
}