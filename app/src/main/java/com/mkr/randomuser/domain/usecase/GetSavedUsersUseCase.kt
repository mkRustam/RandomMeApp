package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import javax.inject.Inject

interface GetSavedUsersUseCase {
    suspend operator fun invoke(): List<User>
}

class GetSavedUsersUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : GetSavedUsersUseCase {
    override suspend operator fun invoke(): List<User> {
        return userRepository.getSavedUsers()
    }
}
