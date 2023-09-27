package com.apex.codeassesment.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResponse
import com.apex.codeassesment.ui.main.MainViewState.Error
import com.apex.codeassesment.ui.main.MainViewState.Loading
import com.apex.codeassesment.ui.main.MainViewState.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var apiResponseObserver: Observer<MainViewState>

    private lateinit var mainViewModel: MainViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(userRepository)
        mainViewModel.viewStateMutableLiveData.observeForever(apiResponseObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loadUserList success`() = testDispatcher.runBlockingTest {
        // Given
        val resultLimit = 10
        val users = listOf(User())
        val apiResponse = ApiResponse(results = users)
        `when`(userRepository.getUsers(resultLimit)).thenReturn(apiResponse)

        // When
        mainViewModel.loadUserList(resultLimit)

        // Then
        Mockito.verify(apiResponseObserver).onChanged(Loading)
        Mockito.verify(apiResponseObserver).onChanged(Success(users))
    }

    @Test
    fun `test loadUserList error`() = testDispatcher.runBlockingTest {
        // Given
        val resultLimit = 10
        val errorMessage = "Error message"
        val apiResponse = ApiResponse(null, error = errorMessage)
        `when`(userRepository.getUsers(resultLimit)).thenReturn(apiResponse)

        // When
        mainViewModel.loadUserList(resultLimit)

        // Then
        Mockito.verify(apiResponseObserver).onChanged(Loading)
        Mockito.verify(apiResponseObserver).onChanged(Error(errorMessage))
    }

    @Test
    fun `test onUserListItemClicked`() {
        // Given
        val user = User()
        val successViewState = Success(listOf(user))
        mainViewModel.viewStateMutableLiveData.postValue(successViewState)
        val position = 0

        // When
        mainViewModel.onUserListItemClicked(position)

        // Then
        val observedUser = mainViewModel.randomUser.value
        assert(observedUser == user)
    }

}
