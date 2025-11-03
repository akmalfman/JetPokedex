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

    /** 🔹 Ambil satu halaman daftar Pokemon */
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonPage

    /** 🔹 Ambil detail spesifik seekor Pokemon */
    suspend fun getPokemonDetail(name: String): PokemonDetail

    /** 🔹 Search Pokemon dari data yang ada */
    suspend fun searchPokemon(query: String): List<PokemonEntry>

    /** 🔹 Cek apakah ada data offline */
    fun hasOfflineData(): Boolean

    /** 🔹 Hapus cache dan download ulang */
    suspend fun refreshData()

    // Catatan: Jika getPokemonList, getPokemonDetail, atau searchPokemon
    // bisa gagal (misal: API error & tidak ada cache),
    // kita bisa bungkus return type-nya dengan Result wrapper,
    // tapi untuk sekarang kita buat sederhana dulu.
}