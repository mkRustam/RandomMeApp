package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import javax.inject.Inject

interface GetUserByIdUseCase {
    suspend operator fun invoke(userId: Int): Resource<User>
}

class GetUserByIdUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : GetUserByIdUseCase {
    override suspend operator fun invoke(userId: Int): Resource<User> {
        return userRepository.getUserById(userId)
    }
}
