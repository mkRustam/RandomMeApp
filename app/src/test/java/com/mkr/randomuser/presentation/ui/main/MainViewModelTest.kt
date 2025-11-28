package com.mkr.randomuser.presentation.ui.main

import app.cash.turbine.test
import com.mkr.randomuser.TestData
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.domain.usecase.GetRandomUserUseCase
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
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getRandomUserUseCase: GetRandomUserUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        getRandomUserUseCase = mockk()
        viewModel = MainViewModel(getRandomUserUseCase)
    }

    @Test
    fun `initial state has loading false and no error`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)
            assertNull(initialState.errorMessage)
        }
    }

    @Test
    fun `onGenerateUserClicked sets loading to true then false on success`() = runTest(testDispatcher) {
        val testUser = TestData.createTestUser()
        val gender = "male"
        val nat = "US"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Success(testUser)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)

            viewModel.onGenerateUserClicked(gender, nat)
            advanceUntilIdle()

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
            assertNull(finalState.errorMessage)
        }
    }

    @Test
    fun `onGenerateUserClicked emits NavigateToUserList event on success`() = runTest(testDispatcher) {
        val testUser = TestData.createTestUser()
        val gender = "male"
        val nat = "US"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Success(testUser)

        viewModel.events.test {
            viewModel.onGenerateUserClicked(gender, nat)
            advanceUntilIdle()

            val event = awaitItem()
            assertTrue(event is MainEvent.NavigateToUserList)
        }
    }

    @Test
    fun `onGenerateUserClicked sets error message on failure`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"
        val errorMessage = "Network error"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Error(errorMessage)

        viewModel.uiState.test {
            skipItems(1) // Skip initial state

            viewModel.onGenerateUserClicked(gender, nat)
            advanceUntilIdle()

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.errorMessage)
        }
    }

    @Test
    fun `onGenerateUserClicked uses default error message when error message is blank`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Error("")

        viewModel.uiState.test {
            skipItems(1) // Skip initial state

            viewModel.onGenerateUserClicked(gender, nat)
            advanceUntilIdle()

            val errorState = awaitItem()
            assertEquals("Something went wrong", errorState.errorMessage)
        }
    }

    @Test
    fun `consumeError clears error message`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"
        val errorMessage = "Network error"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Error(errorMessage)

        viewModel.uiState.test {
            skipItems(1) // Skip initial state

            viewModel.onGenerateUserClicked(gender, nat)
            advanceUntilIdle()
            skipItems(1) // Skip loading state

            val errorState = awaitItem()
            assertEquals(errorMessage, errorState.errorMessage)

            viewModel.consumeError()
            val clearedState = awaitItem()
            assertNull(clearedState.errorMessage)
        }
    }

    @Test
    fun `onGenerateUserClicked calls use case with correct parameters`() = runTest(testDispatcher) {
        val testUser = TestData.createTestUser()
        val gender = "female"
        val nat = "CA"

        coEvery { getRandomUserUseCase(gender, nat) } returns Resource.Success(testUser)

        viewModel.onGenerateUserClicked(gender, nat)
        advanceUntilIdle()

        coEvery { getRandomUserUseCase(gender, nat) }
    }
}

