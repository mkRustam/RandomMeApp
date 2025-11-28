package com.mkr.randomuser.core.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SafeCallTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `safeCall returns Success when block succeeds`() = runTest {
        val result = safeCall(testDispatcher) {
            "success"
        }

        assertTrue(result is Resource.Success)
        assertEquals("success", (result as Resource.Success).data)
    }

    @Test
    fun `safeCall returns Error when block throws exception`() = runTest {
        val exception = RuntimeException("Test error")
        val result = safeCall(testDispatcher) {
            throw exception
        }

        assertTrue(result is Resource.Error)
        assertEquals("Test error", (result as Resource.Error).message)
        assertEquals(exception, (result as Resource.Error).throwable)
    }

    @Test
    fun `safeCall returns Error with empty message when exception has no message`() = runTest {
        val exception = RuntimeException()
        val result = safeCall(testDispatcher) {
            throw exception
        }

        assertTrue(result is Resource.Error)
        assertEquals("", (result as Resource.Error).message)
        assertEquals(exception, (result as Resource.Error).throwable)
    }

    @Test
    fun `safeCall rethrows CancellationException`() = runTest {
        val cancellationException = kotlinx.coroutines.CancellationException("Cancelled")
        
        try {
            safeCall(testDispatcher) {
                throw cancellationException
            }
            fail("CancellationException should be rethrown")
        } catch (e: kotlinx.coroutines.CancellationException) {
            assertEquals(cancellationException, e)
        }
    }
}

