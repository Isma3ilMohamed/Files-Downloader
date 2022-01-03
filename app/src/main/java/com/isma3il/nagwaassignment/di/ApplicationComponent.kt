package com.isma3il.nagwaassignment.di

import com.isma3il.nagwaassignment.ui.MainActivity
import dagger.Component

@Component
interface ApplicationComponent {

    fun inject(activity:MainActivity)
}