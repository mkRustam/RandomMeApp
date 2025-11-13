package com.mkr.randomuser.presentation.util

object CountryUtils {
    fun getCountryFlag(countryCode: String): String {
        return countryCode
            .uppercase()
            .map { char ->
                Character.toString(char.code + 127397)
            }
            .joinToString("")
    }
}
