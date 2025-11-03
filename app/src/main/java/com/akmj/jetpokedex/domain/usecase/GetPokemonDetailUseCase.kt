package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String) =
        repository.getPokemonDetail(name = name)
}