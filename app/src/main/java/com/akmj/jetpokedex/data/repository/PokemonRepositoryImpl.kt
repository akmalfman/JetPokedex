package com.akmj.jetpokedex.data.repository

import android.content.Context
import com.akmj.jetpokedex.data.local.PokemonDatabase
import com.akmj.jetpokedex.data.mapper.toPokemonAbility
import com.akmj.jetpokedex.data.mapper.toPokemonDetail // ❗️ IMPORT MAPPER
import com.akmj.jetpokedex.data.mapper.toPokemonEntry  // ❗️ IMPORT MAPPER
import com.akmj.jetpokedex.data.mapper.toPokemonPage   // ❗️ IMPORT MAPPER
import com.akmj.jetpokedex.data.remote.ApiConfig
import com.akmj.jetpokedex.domain.model.PokemonDetail
import com.akmj.jetpokedex.domain.model.PokemonEntry
import com.akmj.jetpokedex.domain.model.PokemonPage
import com.akmj.jetpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ❗️ PERHATIKAN: Sekarang kita implementasi interface
class PokemonRepositoryImpl(private val context: Context) : PokemonRepository {

    private val pokemonDb = PokemonDatabase(context)

    /** 🔹 Ambil Pokemon List dengan strategi offline-first */
    // ❗️ Perhatikan 'override' dan return type 'PokemonPage'
    override suspend fun getPokemonList(offset: Int, limit: Int): PokemonPage {
        return withContext(Dispatchers.IO) {
            try {
                // Coba ambil dari API
                val response = ApiConfig.apiService.getPokemonList(offset = offset, limit = limit)
                val results = response.results?.filterNotNull() ?: emptyList()

                // Simpan DTO ke local database
                if (results.isNotEmpty()) {
                    pokemonDb.savePokemonList(results)
                }

                // ❗️ Kembalikan Entitas Domain MURNI (hasil mapping)
                response.toPokemonPage()

            } catch (e: Exception) {
                // Jika gagal (offline), ambil DTO dari local database
                e.printStackTrace()
                val resultsFromDb = pokemonDb.getPokemonList(limit = limit, offset = offset)

                // ❗️ Mapping DTO dari DB ke Entitas Domain
                val entries = resultsFromDb.map { it.toPokemonEntry() }
                PokemonPage(
                    count = 0, // DB kita tidak menyimpan count
                    next = null, // DB kita tidak menyimpan 'next'
                    pokemonList = entries
                )
            }
        }
    }

    /** 🔹 Ambil Pokemon Detail dengan strategi offline-first */
    // ❗️ Perhatikan 'override' dan return type 'PokemonDetail'
    override suspend fun getPokemonDetail(name: String): PokemonDetail {
        return withContext(Dispatchers.IO) {
            try {
                // Coba ambil dari API
                val response = ApiConfig.apiService.getPokemonDetail(name)
                val abilities = response.abilities?.filterNotNull() ?: emptyList()

                // Simpan DTO ke local database
                if (abilities.isNotEmpty()) {
                    pokemonDb.savePokemonDetail(name, abilities)
                }

                // ❗️ Kembalikan Entitas Domain MURNI (hasil mapping)
                response.toPokemonDetail()

            } catch (e: Exception) {
                // Jika gagal (offline), ambil DTO dari local database
                e.printStackTrace()
                val abilitiesFromDb = pokemonDb.getPokemonDetail(name) ?: emptyList()

                // ❗️ Kita harus membuat 'PokemonDetail' palsu dari data DB
                // Ini sedikit merepotkan karena DB Anda hanya menyimpan 'abilities'
                PokemonDetail(
                    id = 0, // Tidak ada di DB
                    name = name, // Kita hanya punya nama
                    abilities = abilitiesFromDb.map { it.toPokemonAbility() } // ❗️ Mapping DTO
                )
            }
        }
    }

    /** 🔹 Search Pokemon (prioritas local database) */
    // ❗️ Perhatikan 'override' dan return type 'List<PokemonEntry>'
    override suspend fun searchPokemon(query: String): List<PokemonEntry> {
        return withContext(Dispatchers.IO) {
            val resultsFromDb = pokemonDb.searchPokemon(query)

            // ❗️ Mapping DTO dari DB ke Entitas Domain
            resultsFromDb.map { it.toPokemonEntry() }
        }
    }

    /** 🔹 Cek apakah ada data offline */
    // ❗️ 'override' ditambahkan
    override fun hasOfflineData(): Boolean {
        return pokemonDb.hasPokemonData()
    }

    /** 🔹 Refresh data (hapus cache dan download ulang) */
    // ❗️ 'override' ditambahkan
    override suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            pokemonDb.clearAllPokemon()
        }
    }
}