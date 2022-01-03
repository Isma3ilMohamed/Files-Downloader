package com.isma3il.nagwaassignment.data.di

import com.isma3il.nagwaassignment.data.repositories.AppFileRepository
import com.isma3il.nagwaassignment.domain.repositories.FileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun provideRepository(repository: AppFileRepository):FileRepository
}