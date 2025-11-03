package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository
import com.akmj.jetpokedex.domain.util.AuthResult

class LoginUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Email dan password tidak boleh kosong")
        }

        val user = repository.login(email, password)
        return if (user != null) {
            repository.saveSession(user.email)
            AuthResult.Success
        } else {
            AuthResult.Error("Email atau password salah")
        }
    }
}