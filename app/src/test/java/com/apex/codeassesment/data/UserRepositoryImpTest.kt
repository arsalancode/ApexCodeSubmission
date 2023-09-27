package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResponse
import com.apex.codeassesment.data.remote.RemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UserRepositoryImpTest {

    private lateinit var userRepository: UserRepositoryImp
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        localDataSource = mock(LocalDataSource::class.java)
        remoteDataSource = mock(RemoteDataSource::class.java)
        userRepository = UserRepositoryImp(localDataSource, remoteDataSource)
    }

    @Test
    fun `getSavedUser should delegate to local data source`() {
        val user = User.createRandom()
        `when`(localDataSource.loadUser()).thenReturn(user)

        val savedUser = userRepository.getSavedUser()

        assertEquals(user, savedUser)
    }

    @Test
    fun `getUser should force update and return user from remote data source`() = runBlocking {
        val user = User.createRandom()
        `when`(remoteDataSource.loadUser()).thenReturn(user)

        val fetchedUser = userRepository.getUser(forceUpdate = true)

        assertEquals(user, fetchedUser)
        verify(localDataSource).saveUser(user)
    }

    @Test
    fun `getUsers should delegate to remote data source`() = runBlocking {
        val resultLimit = 10
        val apiResponse = ApiResponse(listOf(User.createRandom()))
        `when`(remoteDataSource.loadUsers(resultLimit)).thenReturn(apiResponse)

        val fetchedApiResponse = userRepository.getUsers(resultLimit)

        assertEquals(apiResponse, fetchedApiResponse)
    }
}
