package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class SearchPokemonUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(query: String) =
        repository.searchPokemon(query = query)
}