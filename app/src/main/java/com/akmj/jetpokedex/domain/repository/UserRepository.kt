package com.akmj.jetpokedex.domain.repository

import com.akmj.jetpokedex.domain.model.User

/**
 * Interface (Kontrak) untuk semua operasi terkait User
 */
interface UserRepository {

    // Mengembalikan false jika email sudah ada
    suspend fun register(user: User): Boolean

    // Mengembalikan null jika login gagal
    suspend fun login(email: String, password: String): User?

    suspend fun saveSession(email: String)

    suspend fun logout()

    fun isLoggedIn(): Boolean

    fun getLoggedInEmail(): String?
}