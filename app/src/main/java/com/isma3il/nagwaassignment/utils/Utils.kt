package com.isma3il.nagwaassignment.utils

import android.app.Activity
import android.view.Window
import android.view.WindowManager

object Utils {
    fun fullScreen(activity: Activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}