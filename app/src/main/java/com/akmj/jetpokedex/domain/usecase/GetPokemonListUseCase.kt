package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class GetPokemonListUseCase(
    private val repository: PokemonRepository // Dapatkan interface, bukan implementasi
) {
    suspend operator fun invoke(offset: Int, limit: Int) =
        repository.getPokemonList(offset = offset, limit = limit)
}