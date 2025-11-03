package com.akmj.jetpokedex.data.mapper

import com.akmj.jetpokedex.data.remote.response.AbilitiesItem
import com.akmj.jetpokedex.data.remote.response.PokemonDetailResponse
import com.akmj.jetpokedex.data.remote.response.PokemonListResponse
import com.akmj.jetpokedex.data.remote.response.ResultsItem
import com.akmj.jetpokedex.domain.model.PokemonAbility
import com.akmj.jetpokedex.domain.model.PokemonDetail
import com.akmj.jetpokedex.domain.model.PokemonEntry
import com.akmj.jetpokedex.domain.model.PokemonPage

// --- Mapper untuk List ---

/**
 * Mengubah DTO 'PokemonListResponse' (dari API) menjadi 'PokemonPage' (Entitas Domain).
 */
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

/**
 * Mengubah DTO 'ResultsItem' (dari API atau DB) menjadi 'PokemonEntry' (Entitas Domain).
 */
fun ResultsItem.toPokemonEntry(): PokemonEntry {
    val id = if (this.url != null) {
        this.url.removeSuffix("/").substringAfterLast("/").toInt()
    } else {
        // Handle jika URL null, mungkin dari DB?
        0 // Anda bisa sesuaikan logic ini
    }
    return PokemonEntry(
        name = this.name ?: "Unknown",
        id = id
    )
}

// --- Mapper untuk Detail ---

/**
 * Mengubah DTO 'PokemonDetailResponse' (dari API) menjadi 'PokemonDetail' (Entitas Domain).
 */
fun PokemonDetailResponse.toPokemonDetail(): PokemonDetail {
    val abilities = this.abilities?.filterNotNull()?.map {
        it.toPokemonAbility()
    } ?: emptyList()

    return PokemonDetail(
        id = this.id ?: 0,
        name = this.name ?: "Unknown",
        abilities = abilities
        // Jika nanti Anda butuh gambar, tambahkan di sini:
        // imageUrl = this.sprites?.other?.officialArtwork?.frontDefault ?: ""
    )
}

/**
 * Mengubah DTO 'AbilitiesItem' (dari API atau DB) menjadi 'PokemonAbility' (Entitas Domain).
 */
fun AbilitiesItem.toPokemonAbility(): PokemonAbility =
    PokemonAbility(
        name = this.ability?.name ?: "Unknown",
        isHidden = this.isHidden ?: false
    )