package com.akmj.jetpokedex.viewmodel

// ❗️ HAPUS: import android.content.Context
// ❗️ HAPUS: import com.akmj.jetpokedex.data.repository.PokemonRepositoryImpl
// ❗️ HAPUS: import com.akmj.jetpokedex.data.remote.response.AbilitiesItem

// ❗️ IMPORT BARU: Entitas Domain Murni
import com.akmj.jetpokedex.domain.model.PokemonDetail
// ❗️ IMPORT BARU: Use Case
import com.akmj.jetpokedex.domain.usecase.GetPokemonDetailUseCase
// ❗️ IMPORT BARU: Hilt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    // ❗️ HAPUS: private val repository = PokemonRepositoryImpl(context)

    // ❗️ GANTI TIPE DATA: Dari List DTO ke satu Entitas Domain
    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail: StateFlow<PokemonDetail?> = _pokemonDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // ❗️ Ganti nama fungsi agar lebih jelas
    fun fetchPokemonDetail(name: String) {
        // Jangan fetch jika sudah ada (bisa dihapus jika Anda ingin 'refresh' tiap kali)
        if (_pokemonDetail.value != null && _pokemonDetail.value?.name == name) return

        _isLoading.value = true
        _errorMessage.value = null
        _pokemonDetail.value = null // Kosongkan dulu

        viewModelScope.launch {
            try {
                // ❗️ PANGGIL USE CASE
                val result = getPokemonDetailUseCase(name)

                _pokemonDetail.value = result // Simpan seluruh objek detail
                _isOfflineMode.value = false

            } catch (e: Exception) {
                _errorMessage.value = "Mode Offline - Gagal mengambil detail"
                _isOfflineMode.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}