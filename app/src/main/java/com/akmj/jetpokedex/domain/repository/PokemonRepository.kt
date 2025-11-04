package com.akmj.jetpokedex.domain.repository

import com.akmj.jetpokedex.domain.model.PokemonDetail
import com.akmj.jetpokedex.domain.model.PokemonEntry
import com.akmj.jetpokedex.domain.model.PokemonPage

/**
 * Ini adalah KONTRAK (Interface) yang ada di DOMAIN layer.
 * Perhatikan:
 * 1. Tidak ada dependensi 'android.content.Context'.
 * 2. Semua return type adalah Entitas Domain MURNI kita (PokemonPage, PokemonDetail, dll).
 * 3. ViewModel HANYA akan tahu tentang interface ini, bukan implementasinya.
 */
interface PokemonRepository {

    suspend fun getPokemonList(offset: Int, limit: Int): PokemonPage

    suspend fun getPokemonDetail(name: String): PokemonDetail

    suspend fun searchPokemon(query: String): List<PokemonEntry>

    fun hasOfflineData(): Boolean

    suspend fun refreshData()
}