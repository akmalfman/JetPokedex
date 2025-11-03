package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class CheckOfflineDataUseCase(
    private val repository: PokemonRepository
) {
    // Ini bukan 'suspend' karena fungsi aslinya tidak 'suspend'
    operator fun invoke() = repository.hasOfflineData()
}