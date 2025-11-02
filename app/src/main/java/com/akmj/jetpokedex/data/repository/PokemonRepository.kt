package com.akmj.jetpokedex.data.repository

import com.akmj.jetpokedex.data.remote.ApiConfig
import com.akmj.jetpokedex.domain.model.AbilitiesItem
import com.akmj.jetpokedex.domain.model.ResultsItem

class PokemonRepository {

    suspend fun getPokemonList(offset: Int = 0, limit: Int = 10): List<ResultsItem> {
        val response = ApiConfig.apiService.getPokemonList(offset = offset, limit = limit)
        return response.results?.filterNotNull() ?: emptyList()
    }


    suspend fun getPokemonDetail(name : String): List<AbilitiesItem>{
        val response = ApiConfig.apiService.getPokemonDetail(name)
        return response.abilities?.filterNotNull() ?: emptyList()
    }
}
