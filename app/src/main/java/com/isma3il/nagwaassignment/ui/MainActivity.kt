package com.isma3il.nagwaassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.isma3il.nagwaassignment.R
import com.isma3il.nagwaassignment.data.model.FileResponse
import com.isma3il.nagwaassignment.utils.readAssetsFile

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}