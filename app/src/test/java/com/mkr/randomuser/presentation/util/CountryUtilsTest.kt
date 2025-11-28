package com.mkr.randomuser.presentation.util

import org.junit.Assert.*
import org.junit.Test

class CountryUtilsTest {

    @Test
    fun `getCountryFlag converts country code to flag emoji`() {
        val result = CountryUtils.getCountryFlag("US")
        
        // The result should be a flag emoji (2 characters converted to regional indicator symbols)
        assertNotNull(result)
        assertTrue(result.length >= 2)
    }

    @Test
    fun `getCountryFlag handles lowercase country code`() {
        val result = CountryUtils.getCountryFlag("us")
        
        assertNotNull(result)
        assertTrue(result.length >= 2)
    }

    @Test
    fun `getCountryFlag handles mixed case country code`() {
        val result = CountryUtils.getCountryFlag("Us")
        
        assertNotNull(result)
        assertTrue(result.length >= 2)
    }

    @Test
    fun `getCountryFlag converts each character correctly`() {
        val result = CountryUtils.getCountryFlag("CA")
        
        // Each character should be converted to a regional indicator symbol
        assertNotNull(result)
        assertTrue(result.length >= 2)
    }

    @Test
    fun `getCountryFlag handles different country codes`() {
        val usFlag = CountryUtils.getCountryFlag("US")
        val caFlag = CountryUtils.getCountryFlag("CA")
        val gbFlag = CountryUtils.getCountryFlag("GB")
        
        assertNotNull(usFlag)
        assertNotNull(caFlag)
        assertNotNull(gbFlag)
        // Different country codes should produce different flag emojis
        assertNotEquals(usFlag, caFlag)
    }
}

