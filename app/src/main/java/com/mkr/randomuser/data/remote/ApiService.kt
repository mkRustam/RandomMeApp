package com.mkr.randomuser.data.remote

import com.mkr.randomuser.data.remote.dto.ApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun createRandomUser(
        @Query("gender") gender: String,
        @Query("nat") nationality: String
    ): ApiResponseDto
}