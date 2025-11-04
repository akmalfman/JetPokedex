package com.akmj.jetpokedex.domain.repository

import com.akmj.jetpokedex.domain.model.User

interface UserRepository {

    suspend fun register(user: User): Boolean

    suspend fun login(email: String, password: String): User?

    suspend fun saveSession(email: String)

    suspend fun logout()

    fun isLoggedIn(): Boolean

    fun getLoggedInEmail(): String?

    suspend fun getUserDetails(email: String): User?
}