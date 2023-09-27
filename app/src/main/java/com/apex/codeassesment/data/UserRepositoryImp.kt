package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    private val savedUser = AtomicReference(User())

    override fun getSavedUser() = localDataSource.loadUser()

    override suspend fun getUser(forceUpdate: Boolean): User {
        if (forceUpdate) {
            val user = remoteDataSource.loadUser()
            localDataSource.saveUser(user)
            savedUser.set(user)
        }
        return savedUser.get()
    }

    override suspend fun getUsers(resultLimit: Int) = remoteDataSource.loadUsers(resultLimit)
}
