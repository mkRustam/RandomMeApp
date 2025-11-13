package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import javax.inject.Inject

interface GetRandomUserUseCase {
    suspend operator fun invoke(gender: String, nat: String): User
}

class GetRandomUserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : GetRandomUserUseCase {
    override suspend operator fun invoke(gender: String, nat: String): User {
        return userRepository.createRandomUser(gender, nat)
    }
}
