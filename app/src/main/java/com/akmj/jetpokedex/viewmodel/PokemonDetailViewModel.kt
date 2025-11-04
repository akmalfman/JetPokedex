package com.akmj.jetpokedex.viewmodel

import com.akmj.jetpokedex.domain.model.PokemonDetail
import com.akmj.jetpokedex.domain.usecase.GetPokemonDetailUseCase
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

    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail: StateFlow<PokemonDetail?> = _pokemonDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
s
    fun fetchPokemonDetail(name: String) {

        if (_pokemonDetail.value != null && _pokemonDetail.value?.name == name) return

        _isLoading.value = true
        _errorMessage.value = null
        _pokemonDetail.value = null

        viewModelScope.launch {
            try {
                val result = getPokemonDetailUseCase(name)

                _pokemonDetail.value = result
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