package com.mkr.randomuser.presentation.ui.details

import app.cash.turbine.test
import com.mkr.randomuser.TestData
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.domain.usecase.GetUserByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var savedStateHandle: androidx.lifecycle.SavedStateHandle
    private lateinit var viewModel: UserDetailsViewModel

    @Before
    fun setup() {
        getUserByIdUseCase = mockk()
    }

    @Test
    fun `initial state has loading true and no user`() = runTest(testDispatcher) {
        val userId = 1
        val testUser = TestData.createTestUser(id = userId)

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Success(testUser)

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.user)
            assertNull(initialState.errorMessage)
        }
    }

    @Test
    fun `loadUser sets user and loading false on success`() = runTest(testDispatcher) {
        val userId = 1
        val testUser = TestData.createTestUser(id = userId)

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Success(testUser)

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNotNull(successState.user)
            assertEquals(testUser.id, successState.user?.id)
            assertEquals(testUser.email, successState.user?.email)
            assertNull(successState.errorMessage)
        }
    }

    @Test
    fun `loadUser sets error message on failure`() = runTest(testDispatcher) {
        val userId = 1
        val errorMessage = "User not found"

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Error(errorMessage)

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.user)
            assertEquals(errorMessage, errorState.errorMessage)
        }
    }

    @Test
    fun `loadUser uses default error message when error message is blank`() = runTest(testDispatcher) {
        val userId = 1

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Error("")

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val errorState = awaitItem()
            assertEquals("Unable to load user", errorState.errorMessage)
        }
    }

    @Test
    fun `consumeError clears error message`() = runTest(testDispatcher) {
        val userId = 1
        val errorMessage = "User not found"

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Error(errorMessage)

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state

            val errorState = awaitItem()
            assertEquals(errorMessage, errorState.errorMessage)

            viewModel.consumeError()
            val clearedState = awaitItem()
            assertNull(clearedState.errorMessage)
        }
    }

    @Test
    fun `loadUser calls use case with correct userId`() = runTest(testDispatcher) {
        val userId = 5
        val testUser = TestData.createTestUser(id = userId)

        savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("userId" to userId))
        coEvery { getUserByIdUseCase(userId) } returns Resource.Success(testUser)

        viewModel = UserDetailsViewModel(getUserByIdUseCase, savedStateHandle)
        advanceUntilIdle()

        coEvery { getUserByIdUseCase(userId) }
    }
}

