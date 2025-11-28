package com.mkr.randomuser.core.common

import org.junit.Assert.*
import org.junit.Test

class ResourceTest {

    @Test
    fun `Resource Success contains data`() {
        val data = "test data"
        val resource = Resource.Success(data)

        assertTrue(resource is Resource.Success)
        assertEquals(data, (resource as Resource.Success).data)
    }

    @Test
    fun `Resource Error contains message and throwable`() {
        val message = "Error message"
        val throwable = RuntimeException("Test exception")
        val resource = Resource.Error(message, throwable)

        assertTrue(resource is Resource.Error)
        assertEquals(message, (resource as Resource.Error).message)
        assertEquals(throwable, (resource as Resource.Error).throwable)
    }

    @Test
    fun `Resource Error can be created without throwable`() {
        val message = "Error message"
        val resource = Resource.Error(message)

        assertTrue(resource is Resource.Error)
        assertEquals(message, (resource as Resource.Error).message)
        assertNull((resource as Resource.Error).throwable)
    }

    @Test
    fun `Resource Loading is singleton`() {
        val loading1 = Resource.Loading
        val loading2 = Resource.Loading

        assertTrue(loading1 is Resource.Loading)
        assertTrue(loading2 is Resource.Loading)
        assertSame(loading1, loading2)
    }

    @Test
    fun `Resource map transforms Success data`() {
        val resource = Resource.Success(5)
        val mapped = resource.map { it * 2 }

        assertTrue(mapped is Resource.Success)
        assertEquals(10, (mapped as Resource.Success).data)
    }

    @Test
    fun `Resource map preserves Error`() {
        val resource = Resource.Error("Error message")
        val mapped = resource.map { it * 2 }

        assertTrue(mapped is Resource.Error)
        assertEquals("Error message", (mapped as Resource.Error).message)
    }

    @Test
    fun `Resource map preserves Loading`() {
        val resource = Resource.Loading
        val mapped = resource.map { it * 2 }

        assertTrue(mapped is Resource.Loading)
    }
}

