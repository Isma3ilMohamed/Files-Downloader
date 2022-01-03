package com.isma3il.nagwaassignment.ui

import androidx.lifecycle.ViewModel
import com.isma3il.nagwaassignment.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainTaskModule {


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel):ViewModel
}