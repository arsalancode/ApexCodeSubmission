package com.apex.codeassesment.ui.main

import com.apex.codeassesment.data.model.User

sealed class MainViewState {
    object Loading : MainViewState()
    data class Success(val users: List<User>) : MainViewState()
    data class Error(val message: String) : MainViewState()
}
