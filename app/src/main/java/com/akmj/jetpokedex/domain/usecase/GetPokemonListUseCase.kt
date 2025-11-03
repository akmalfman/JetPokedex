package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.PokemonRepository

class GetPokemonListUseCase(
    private val repository: PokemonRepository // Dapatkan interface, bukan implementasi
) {
    /**
     * 'operator fun invoke' mengizinkan kita memanggil kelas ini
     * seolah-olah dia adalah sebuah fungsi.
     * * Contoh: getPokemonListUseCase(offset = 0, limit = 20)
     */
    suspend operator fun invoke(offset: Int, limit: Int) =
        repository.getPokemonList(offset = offset, limit = limit)
}