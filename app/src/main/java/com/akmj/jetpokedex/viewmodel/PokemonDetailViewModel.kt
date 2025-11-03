package com.akmj.jetpokedex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmj.jetpokedex.data.repository.PokemonRepository
import com.akmj.jetpokedex.domain.model.AbilitiesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(context: Context) : ViewModel() {

    private val repository = PokemonRepository(context)

    private val _abilityList = MutableStateFlow<List<AbilitiesItem>>(emptyList())
    val abilityList: StateFlow<List<AbilitiesItem>> = _abilityList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchAbilityList(name: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = repository.getPokemonDetail(name)
                _abilityList.value = result

                if (result.isEmpty()) {
                    _errorMessage.value = "Tidak ada data abilities"
                    _isOfflineMode.value = true
                } else {
                    _isOfflineMode.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Mode Offline - Menampilkan data tersimpan"
                _isOfflineMode.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}