package com.apex.codeassesment.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.main.MainViewState.Error
import com.apex.codeassesment.ui.main.MainViewState.Loading
import com.apex.codeassesment.ui.main.MainViewState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val randomUser = MutableLiveData<User>()
    val viewStateMutableLiveData = MutableLiveData<MainViewState>()

    init {
        loadRandomUser()
    }

    fun refreshRandomUser() {
        loadRandomUser()
    }

    fun loadUserList(resultLimit: Int) {
        viewStateMutableLiveData.postValue(Loading)

        viewModelScope.launch {
            val apiResponse = userRepository.getUsers(resultLimit)
            //Log.i(javaClass.name, "Result: $apiResponse")

            apiResponse.results?.let {
                viewStateMutableLiveData.postValue(Success(it))
            }

            apiResponse.error?.let {
                // Notify User of an error occurred
                //Log.e(javaClass.name, it)
                viewStateMutableLiveData.postValue(Error(it))
            }
        }
    }

    fun onUserListItemClicked(position: Int) {
        val user = (viewStateMutableLiveData.value as Success).users.getOrNull(position)
        user?.let {
            randomUser.postValue(it)
        }
    }

    private fun loadRandomUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = (viewStateMutableLiveData.value as Success?)?.users?.randomOrNull()
                ?: userRepository.getUser(true)
            randomUser.postValue(user)
        }
    }
}
