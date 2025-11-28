package com.mkr.randomuser.domain.usecase

import com.mkr.randomuser.TestData
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSavedUsersUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: GetSavedUsersUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSavedUsersUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns flow of users from repository`() = runTest {
        val testUsers = TestData.createTestUsers(3)

        every { repository.getSavedUsers() } returns flowOf(testUsers)

        val result = useCase()
        val collectedUsers = mutableListOf<User>()
        result.collect { collectedUsers.addAll(it) }

        assertEquals(testUsers, collectedUsers)
    }

    @Test
    fun `invoke returns empty flow when repository returns empty list`() = runTest {
        every { repository.getSavedUsers() } returns flowOf(emptyList())

        val result = useCase()
        val collectedUsers = mutableListOf<List<User>>()
        result.collect { collectedUsers.add(it) }

        assertEquals(1, collectedUsers.size)
        assertTrue(collectedUsers.first().isEmpty())
    }

    @Test
    fun `invoke calls repository getSavedUsers`() = runTest {
        every { repository.getSavedUsers() } returns flowOf(emptyList())

        useCase().collect { }

        every { repository.getSavedUsers() }
    }
}

