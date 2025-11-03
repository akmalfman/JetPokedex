package com.akmj.jetpokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val abilities: List<PokemonAbility>
)