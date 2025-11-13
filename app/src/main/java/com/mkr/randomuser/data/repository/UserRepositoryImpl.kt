package com.mkr.randomuser.data.repository

import com.mkr.randomuser.data.local.UserDao
import com.mkr.randomuser.data.remote.ApiService
import com.mkr.randomuser.data.toDomain
import com.mkr.randomuser.data.toEntity
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun createRandomUser(gender: String, nat: String): User {
        val userDto = apiService.createRandomUser(gender, nat).results.first()
        val userEntity = userDto.toEntity()
        val newUserId = userDao.insertUser(userEntity)
        return userDao.getUserById(newUserId.toInt()).toDomain()
    }

    override suspend fun getSavedUsers(): List<User> {
        return userDao.getAllUsers().map { it.toDomain() }
    }

    override suspend fun getUserById(userId: Int): User {
        return userDao.getUserById(userId).toDomain()
    }
}