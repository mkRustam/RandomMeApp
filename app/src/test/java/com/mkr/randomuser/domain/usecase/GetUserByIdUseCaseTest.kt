package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.TestData
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserByIdUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetUserByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUserByIdUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns Success when repository finds user`() = runTest {
        val userId = 1
        val testUser = TestData.createTestUser(id = userId)

        coEvery { repository.getUserById(userId) } returns Resource.Success(testUser)

        val result = useCase(userId)

        assertTrue(result is Resource.Success)
        assertEquals(testUser, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        val userId = 1
        val errorMessage = "User not found"

        coEvery { repository.getUserById(userId) } returns Resource.Error(errorMessage)

        val result = useCase(userId)

        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

    @Test
    fun `invoke calls repository with correct userId`() = runTest {
        val userId = 5
        val testUser = TestData.createTestUser(id = userId)

        coEvery { repository.getUserById(userId) } returns Resource.Success(testUser)

        useCase(userId)

        coEvery { repository.getUserById(userId) }
    }
}

