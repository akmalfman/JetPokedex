package com.akmj.jetpokedex.data.repository

import android.content.Context
import com.akmj.jetpokedex.data.local.PokemonDatabase
import com.akmj.jetpokedex.data.remote.ApiConfig
import com.akmj.jetpokedex.domain.model.AbilitiesItem
import com.akmj.jetpokedex.domain.model.ResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val context: Context) {

    private val pokemonDb = PokemonDatabase(context)

    /** 🔹 Ambil Pokemon List dengan strategi offline-first */
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 10): List<ResultsItem> {
        return withContext(Dispatchers.IO) {
            try {
                // Coba ambil dari API
                val response = ApiConfig.apiService.getPokemonList(offset = offset, limit = limit)
                val results = response.results?.filterNotNull() ?: emptyList()

                // Simpan ke local database
                if (results.isNotEmpty()) {
                    pokemonDb.savePokemonList(results)
                }

                results
            } catch (e: Exception) {
                // Jika gagal (offline), ambil dari local database
                e.printStackTrace()
                pokemonDb.getPokemonList(limit = limit, offset = offset)
            }
        }
    }

    /** 🔹 Ambil Pokemon Detail dengan strategi offline-first */
    suspend fun getPokemonDetail(name: String): List<AbilitiesItem> {
        return withContext(Dispatchers.IO) {
            try {
                // Coba ambil dari API
                val response = ApiConfig.apiService.getPokemonDetail(name)
                val abilities = response.abilities?.filterNotNull() ?: emptyList()

                // Simpan ke local database
                if (abilities.isNotEmpty()) {
                    pokemonDb.savePokemonDetail(name, abilities)
                }

                abilities
            } catch (e: Exception) {
                // Jika gagal (offline), ambil dari local database
                e.printStackTrace()
                pokemonDb.getPokemonDetail(name) ?: emptyList()
            }
        }
    }

    /** 🔹 Search Pokemon (prioritas local database) */
    suspend fun searchPokemon(query: String): List<ResultsItem> {
        return withContext(Dispatchers.IO) {
            pokemonDb.searchPokemon(query)
        }
    }

    /** 🔹 Cek apakah ada data offline */
    fun hasOfflineData(): Boolean {
        return pokemonDb.hasPokemonData()
    }

    /** 🔹 Refresh data (hapus cache dan download ulang) */
    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            pokemonDb.clearAllPokemon()
        }
    }
}