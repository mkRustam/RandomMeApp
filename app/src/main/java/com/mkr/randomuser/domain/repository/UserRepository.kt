package com.mkr.randomuser.domain.repository

import com.mkr.randomuser.domain.model.User

interface UserRepository {
    suspend fun createRandomUser(gender: String, nat: String): User
    suspend fun getSavedUsers(): List<User>
    suspend fun getUserById(userId: Int): User
}