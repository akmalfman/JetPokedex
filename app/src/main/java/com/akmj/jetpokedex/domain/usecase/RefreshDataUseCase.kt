package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class RefreshDataUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke() = repository.refreshData()
}