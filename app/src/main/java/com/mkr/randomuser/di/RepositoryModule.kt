package com.mkr.randomuser.di

import com.mkr.randomuser.core.coroutines.DispatcherProvider
import com.mkr.randomuser.data.local.UserDao
import com.mkr.randomuser.data.remote.ApiService
import com.mkr.randomuser.data.repository.UserRepositoryImpl
import com.mkr.randomuser.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao,
        dispatcherProvider: DispatcherProvider
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao, dispatcherProvider)
    }
}