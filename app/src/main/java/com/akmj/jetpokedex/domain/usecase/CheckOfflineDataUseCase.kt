package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class CheckOfflineDataUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke() = repository.hasOfflineData()
}