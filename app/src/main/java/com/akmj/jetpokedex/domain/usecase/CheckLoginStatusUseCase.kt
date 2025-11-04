package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository

class CheckLoginStatusUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}