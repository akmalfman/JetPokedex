package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.repository.UserRepository

class GetLoggedInEmailUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): String? {
        return repository.getLoggedInEmail()
    }
}