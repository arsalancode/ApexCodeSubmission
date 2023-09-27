package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.User
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun loadUser(): User =
        apiService.loadUsers(1).results?.getOrNull(0) ?: User.createRandom()

    suspend fun loadUsers(resultLimit: Int): ApiResponse =
        apiService.loadUsers(resultLimit)

}
