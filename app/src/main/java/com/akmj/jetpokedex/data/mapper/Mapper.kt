package com.akmj.jetpokedex.data.mapper

import com.akmj.jetpokedex.data.remote.response.AbilitiesItem
import com.akmj.jetpokedex.data.remote.response.PokemonDetailResponse
import com.akmj.jetpokedex.data.remote.response.PokemonListResponse
import com.akmj.jetpokedex.data.remote.response.ResultsItem
import com.akmj.jetpokedex.domain.model.PokemonAbility
import com.akmj.jetpokedex.domain.model.PokemonDetail
import com.akmj.jetpokedex.domain.model.PokemonEntry
import com.akmj.jetpokedex.domain.model.PokemonPage

fun PokemonListResponse.toPokemonPage(): PokemonPage {
    val entries = this.results?.filterNotNull()?.map {
        it.toPokemonEntry()
    } ?: emptyList()

    return PokemonPage(
        count = this.count ?: 0,
        next = this.next,
        pokemonList = entries
    )
}

fun ResultsItem.toPokemonEntry(): PokemonEntry {
    val id = if (this.url != null) {
        this.url.removeSuffix("/").substringAfterLast("/").toInt()
    } else {
       0
    }
    return PokemonEntry(
        name = this.name ?: "Unknown",
        id = id
    )
}

fun PokemonDetailResponse.toPokemonDetail(): PokemonDetail {
    val abilities = this.abilities?.filterNotNull()?.map {
        it.toPokemonAbility()
    } ?: emptyList()

    return PokemonDetail(
        id = this.id ?: 0,
        name = this.name ?: "Unknown",
        abilities = abilities
    )
}

fun AbilitiesItem.toPokemonAbility(): PokemonAbility =
    PokemonAbility(
        name = this.ability?.name ?: "Unknown",
        isHidden = this.isHidden ?: false
    )