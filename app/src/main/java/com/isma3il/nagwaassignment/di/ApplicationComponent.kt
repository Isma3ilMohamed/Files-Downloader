package com.isma3il.nagwaassignment.di

import android.content.Context
import com.isma3il.nagwaassignment.data.di.NetworkModule
import com.isma3il.nagwaassignment.data.di.RepoModule
import com.isma3il.nagwaassignment.ui.MainActivity
import com.isma3il.nagwaassignment.ui.MainTaskModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class,
        RepoModule::class,
        ViewModelBuilderModule::class,
        MainTaskModule::class]
)
interface ApplicationComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: MainActivity)
}