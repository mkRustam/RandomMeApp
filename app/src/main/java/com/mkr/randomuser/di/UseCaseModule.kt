package com.mkr.randomuser.di

import com.mkr.randomuser.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetUserByIdUseCase(useCase: GetUserByIdUseCaseImpl): GetUserByIdUseCase

    @Binds
    abstract fun bindGetRandomUserUseCase(useCase: GetRandomUserUseCaseImpl): GetRandomUserUseCase

    @Binds
    abstract fun bindGetSavedUsersUseCase(useCase: GetSavedUsersUseCaseImpl): GetSavedUsersUseCase
}