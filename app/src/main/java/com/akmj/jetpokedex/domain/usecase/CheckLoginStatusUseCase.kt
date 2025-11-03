package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository

class CheckLoginStatusUseCase(
    private val repository: UserRepository
) {
    // Ini tidak suspend, sama seperti fungsi aslinya
    operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}