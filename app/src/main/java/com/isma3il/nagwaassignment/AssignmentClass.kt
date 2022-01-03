package com.isma3il.nagwaassignment

import android.app.Application
import com.isma3il.nagwaassignment.di.DaggerApplicationComponent

class AssignmentClass:Application() {

    val appComponent=DaggerApplicationComponent.create()

    override fun onCreate() {
        super.onCreate()

    }
}