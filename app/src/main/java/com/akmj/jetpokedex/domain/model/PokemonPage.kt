package com.akmj.jetpokedex.domain.model

data class PokemonPage(
    val count: Int,
    val next: String?,
    val pokemonList: List<PokemonEntry>
)