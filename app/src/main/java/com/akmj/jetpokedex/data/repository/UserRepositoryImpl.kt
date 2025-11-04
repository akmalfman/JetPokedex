package com.akmj.jetpokedex.data.repository

import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.data.local.UserSession
import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDb: UserDatabase,
    private val session: UserSession
) : UserRepository {

    override suspend fun register(user: User): Boolean = withContext(Dispatchers.IO) {
        userDb.registerUser(user)
    }

    override suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        userDb.loginUser(email, password)
    }

    override suspend fun saveSession(email: String) = withContext(Dispatchers.IO) {
        session.saveUser(email)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        session.logout()
    }

    override fun isLoggedIn(): Boolean {
        return session.isLoggedIn()
    }

    override fun getLoggedInEmail(): String? {
        return session.getLoggedInEmail()
    }

    override suspend fun getUserDetails(email: String): User? = withContext(Dispatchers.IO) {
        userDb.getUserByEmail(email)
    }
}