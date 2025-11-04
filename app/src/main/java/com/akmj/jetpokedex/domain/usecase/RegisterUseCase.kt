package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.domain.repository.UserRepository
import com.akmj.jetpokedex.domain.util.AuthResult
import java.util.UUID

class RegisterUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): AuthResult {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Semua kolom wajib diisi")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthResult.Error("Format email tidak valid")
        }
        if (password.length < 6) {
            return AuthResult.Error("Password minimal 6 karakter")
        }

        val user = User(UUID.randomUUID().toString(), username, email, password)
        val success = repository.register(user)

        return if (success) {
            AuthResult.Success
        } else {
            AuthResult.Error("Email sudah terdaftar")
        }
    }
}