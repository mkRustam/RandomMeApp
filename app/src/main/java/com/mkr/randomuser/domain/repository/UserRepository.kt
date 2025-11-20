package com.mkr.randomuser.domain.repository

import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createRandomUser(gender: String, nat: String): Resource<User>
    fun getSavedUsers(): Flow<List<User>>
    suspend fun getUserById(userId: Int): Resource<User>
}