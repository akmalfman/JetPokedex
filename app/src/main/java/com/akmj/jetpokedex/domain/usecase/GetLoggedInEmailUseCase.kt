package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository

class GetLoggedInEmailUseCase(
    private val repository: UserRepository
) {
    // Ini tidak suspend, karena SharedPreferences tidak suspend
    operator fun invoke(): String? {
        return repository.getLoggedInEmail()
    }
}