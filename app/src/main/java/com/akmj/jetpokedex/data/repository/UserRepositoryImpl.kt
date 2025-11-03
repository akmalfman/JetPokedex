package com.akmj.jetpokedex.data.repository

import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.data.local.UserSession
import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementasi dari UserRepository.
 * Class ini yang tahu soal 'UserDatabase' dan 'UserSession'.
 *
 * ❗️ Kita tambahkan @Inject constructor agar Hilt tahu cara membuat class ini
 * (walaupun kita akan mendaftarkannya di AppModule secara manual)
 */
class UserRepositoryImpl @Inject constructor(
    private val userDb: UserDatabase,
    private val session: UserSession
) : UserRepository {

    // Kita bungkus dengan IO Dispatcher untuk operasi database/session
    override suspend fun register(user: User): Boolean = withContext(Dispatchers.IO) {
        userDb.registerUser(user)
    }

    override suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        userDb.loginUser(email, password)
    }

    override suspend fun saveSession(email: String) = withContext(Dispatchers.IO) {
        session.saveUser(email)
    }

    // ❗️ Logika SharedPreferences dari ViewModel pindah ke sini
    override suspend fun logout() = withContext(Dispatchers.IO) {
        // Kita panggil fungsi logout di UserSession
        // Asumsi UserSession punya fungsi logout()
        // Jika tidak, Anda bisa salin logic SharedPreferences ke sini
        session.logout()
    }

    override fun isLoggedIn(): Boolean {
        // Ini tidak perlu 'suspend' karena SharedPreferences cepat
        return session.isLoggedIn()
    }

    override fun getLoggedInEmail(): String? {
        // Kita delegasikan tugasnya ke UserSession
        return session.getLoggedInEmail()
    }
}