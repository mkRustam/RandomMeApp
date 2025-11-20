package com.mkr.randomuser.data.repository

import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.core.common.safeCall
import com.mkr.randomuser.core.coroutines.DispatcherProvider
import com.mkr.randomuser.data.local.UserDao
import com.mkr.randomuser.data.remote.ApiService
import com.mkr.randomuser.data.toDomain
import com.mkr.randomuser.data.toEntity
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val dispatcherProvider: DispatcherProvider
) : UserRepository {

    override suspend fun createRandomUser(gender: String, nat: String): Resource<User> {
        return safeCall(dispatcherProvider.io) {
            val userDto = apiService.createRandomUser(gender, nat).results.firstOrNull()
                ?: error("No user returned from API")
            val userEntity = userDto.toEntity()
            val newUserId = userDao.insertUser(userEntity)
            userDao.getUserById(newUserId.toInt()).toDomain()
        }
    }

    override fun getSavedUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getUserById(userId: Int): Resource<User> {
        return safeCall(dispatcherProvider.io) {
            userDao.getUserById(userId).toDomain()
        }
    }
}