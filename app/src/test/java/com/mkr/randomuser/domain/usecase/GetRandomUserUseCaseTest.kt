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
class GetRandomUserUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetRandomUserUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetRandomUserUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns Success when repository succeeds`() = runTest {
        val testUser = TestData.createTestUser()
        val gender = "male"
        val nat = "US"

        coEvery { repository.createRandomUser(gender, nat) } returns Resource.Success(testUser)

        val result = useCase(gender, nat)

        assertTrue(result is Resource.Success)
        assertEquals(testUser, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        val gender = "male"
        val nat = "US"
        val errorMessage = "Network error"

        coEvery { repository.createRandomUser(gender, nat) } returns Resource.Error(errorMessage)

        val result = useCase(gender, nat)

        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        val testUser = TestData.createTestUser()
        val gender = "female"
        val nat = "CA"

        coEvery { repository.createRandomUser(gender, nat) } returns Resource.Success(testUser)

        useCase(gender, nat)

        coEvery { repository.createRandomUser(gender, nat) }
    }
}

