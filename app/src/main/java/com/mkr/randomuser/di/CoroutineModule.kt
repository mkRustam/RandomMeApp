package com.mkr.randomuser.di

import com.mkr.randomuser.core.coroutines.DefaultDispatcherProvider
import com.mkr.randomuser.core.coroutines.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineModule {
    @Binds
    abstract fun bindDispatcherProvider(default: DefaultDispatcherProvider): DispatcherProvider
}

