package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository

class LogoutUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}