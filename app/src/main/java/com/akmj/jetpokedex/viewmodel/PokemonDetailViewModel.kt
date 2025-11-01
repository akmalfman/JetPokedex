package com.akmj.jetpokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmj.jetpokedex.data.repository.PokemonRepository
import com.akmj.jetpokedex.domain.model.AbilitiesItem
import com.akmj.jetpokedex.domain.model.ResultsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel: ViewModel() {

    private val repository = PokemonRepository()

    private val _abilityList = MutableStateFlow<List<AbilitiesItem>>(emptyList())
    val abilityList: StateFlow<List<AbilitiesItem>> = _abilityList

    fun fetchAbilityList(name : String) {
        viewModelScope.launch {
            val result = repository.getPokemonDetail(name)
            _abilityList.value = result
        }
    }
}
