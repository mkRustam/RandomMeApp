package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSavedUsersUseCase {
    operator fun invoke(): Flow<List<User>>
}

class GetSavedUsersUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : GetSavedUsersUseCase {
    override operator fun invoke(): Flow<List<User>> {
        return userRepository.getSavedUsers()
    }
}
