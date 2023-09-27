package com.apex.codeassesment.data

import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResponse

interface UserRepository{
  fun getSavedUser() : User
  suspend fun getUser(forceUpdate: Boolean): User
  suspend fun getUsers(resultLimit: Int) : ApiResponse
}
