package com.akmj.jetpokedex.domain.model

data class PokemonPage(
    val count: Int,
    val next: String?, // URL untuk 'load more', ini boleh null
    val pokemonList: List<PokemonEntry>
)