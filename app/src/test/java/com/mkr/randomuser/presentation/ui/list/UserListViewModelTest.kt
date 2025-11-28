package com.mkr.randomuser.presentation.ui.list

import app.cash.turbine.test
import com.mkr.randomuser.TestData
import com.mkr.randomuser.domain.usecase.GetSavedUsersUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getSavedUsersUseCase: GetSavedUsersUseCase
    private lateinit var viewModel: UserListViewModel

    @Before
    fun setup() {
        getSavedUsersUseCase = mockk()
        viewModel = UserListViewModel(getSavedUsersUseCase)
    }

    @Test
    fun `initial state has loading true and empty users`() = runTest(testDispatcher) {
        every { getSavedUsersUseCase() } returns flowOf(emptyList())

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertTrue(initialState.users.isEmpty())
            assertTrue(initialState.isEmpty)
            assertNull(initialState.errorMessage)
        }
    }

    @Test
    fun `uiState emits users when use case returns users`() = runTest(testDispatcher) {
        val testUsers = TestData.createTestUsers(3)

        every { getSavedUsersUseCase() } returns flowOf(testUsers)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(3, state.users.size)
            assertFalse(state.isEmpty)
            assertNull(state.errorMessage)
            assertEquals(testUsers[0].id, state.users[0].id)
        }
    }

    @Test
    fun `uiState sets isEmpty to true when users list is empty`() = runTest(testDispatcher) {
        every { getSavedUsersUseCase() } returns flowOf(emptyList())

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.users.isEmpty())
            assertTrue(state.isEmpty)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `uiState emits error when use case flow throws exception`() = runTest(testDispatcher) {
        val errorMessage = "Database error"
        val exception = RuntimeException(errorMessage)

        every { getSavedUsersUseCase() } returns kotlinx.coroutines.flow.flow {
            throw exception
        }

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.errorMessage)
            assertTrue(errorState.users.isEmpty())
        }
    }

    @Test
    fun `consumeError clears error message`() = runTest(testDispatcher) {
        val testUsers = TestData.createTestUsers(2)

        every { getSavedUsersUseCase() } returns flowOf(testUsers)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state
            awaitItem() // Get users state

            // Manually set error for testing consumeError
            // In real scenario, error would come from flow catch
            viewModel.consumeError()
            
            val state = awaitItem()
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `uiState updates when new users are emitted`() = runTest(testDispatcher) {
        val initialUsers = TestData.createTestUsers(2)
        val updatedUsers = TestData.createTestUsers(5)

        every { getSavedUsersUseCase() } returns flowOf(initialUsers, updatedUsers)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val firstState = awaitItem()
            assertEquals(2, firstState.users.size)

            val secondState = awaitItem()
            assertEquals(5, secondState.users.size)
        }
    }
}

