package com.apex.codeassesment.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences?) {

    companion object {
        const val SAVED_USER = "saved-user"
    }

    fun saveUser(user: String) = sharedPreferences?.edit()?.putString(SAVED_USER, user)?.apply()

    fun loadUser(): String? = sharedPreferences?.getString(SAVED_USER, null)

}
