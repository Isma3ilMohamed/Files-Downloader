package com.isma3il.nagwaassignment.data.di

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideAssetManager(context: Context):AssetManager{
        return context.assets
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}