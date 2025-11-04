package com.akmj.jetpokedex.domain.usecase

import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val repository: UserRepository,
    private val getLoggedInEmailUseCase: GetLoggedInEmailUseCase
) {
    suspend operator fun invoke(): User? {
        val email = getLoggedInEmailUseCase() ?: return null

        return repository.getUserDetails(email)
    }
}