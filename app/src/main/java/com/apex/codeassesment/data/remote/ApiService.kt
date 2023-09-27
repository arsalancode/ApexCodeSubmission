package com.apex.codeassesment.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun loadUsers(@Query("results") results: Int): ApiResponse
}
