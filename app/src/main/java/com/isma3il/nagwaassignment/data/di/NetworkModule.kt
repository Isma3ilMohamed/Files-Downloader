package com.isma3il.nagwaassignment.data.di

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.isma3il.nagwaassignment.data.remote.Api
import com.isma3il.nagwaassignment.data.remote.interceptors.DownloadProgressInterceptor
import com.isma3il.nagwaassignment.data.remote.interceptors.LoggingInterceptor
import com.isma3il.nagwaassignment.utils.Constants

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideAssetManager(context: Context): AssetManager {
        return context.assets
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }

    @Singleton
    @Provides
    fun createApi(okHttpClient: OkHttpClient): Api {
        return Retrofit.Builder()
            .baseUrl(Constants.FAKE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(Api::class.java)
    }


    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        downloadProgressInterceptor: DownloadProgressInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addNetworkInterceptor(downloadProgressInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(loggingInterceptor)

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }
}