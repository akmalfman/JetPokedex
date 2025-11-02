package com.akmj.jetpokedex.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.data.local.UserSession
import com.akmj.jetpokedex.domain.model.User
import java.util.*

class LoginRegisterViewModel(
    val context: Context
) : ViewModel() {

    private val userDb = UserDatabase(context)
    private val session = UserSession(context) // ✅ gunakan helper class

    var loginState = mutableStateOf(session.isLoggedIn())
        private set

    var errorMessage = mutableStateOf<String?>(null)

    fun register(username: String, email: String, password: String) {
        // Cek apakah ada field kosong
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage.value = "Semua kolom wajib diisi"
            return
        }

        // Validasi email sederhana
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.value = "Format email tidak valid"
            return
        }

        // Minimal panjang password
        if (password.length < 6) {
            errorMessage.value = "Password minimal 6 karakter"
            return
        }

        // Cek apakah email sudah terdaftar
        val user = User(UUID.randomUUID().toString(), username, email, password)
        val success = userDb.registerUser(user)

        if (!success) {
            errorMessage.value = "Email sudah terdaftar"
        } else {
            errorMessage.value = null
        }
    }


    fun login(email: String, password: String): User? {
        val user = userDb.loginUser(email, password)
        if (user != null) {
            session.saveUser(user.email) // ✅ simpan session
            loginState.value = true
            errorMessage.value = null
        } else {
            errorMessage.value = "Email atau password salah"
        }
        return user
    }

    fun logout() {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.edit().remove("email").apply()
        loginState.value = false
    }
}
