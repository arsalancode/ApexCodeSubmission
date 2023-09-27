package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.model.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val moshi: Moshi
) {
    fun loadUser(): User {
        val serializedUser = preferencesManager.loadUser()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        return serializedUser?.let {
            jsonAdapter.fromJson(it)
        } ?: User.createRandom()
    }

    fun saveUser(user: User) {
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        val serializedUser = jsonAdapter.toJson(user)
        preferencesManager.saveUser(serializedUser)
    }
}
