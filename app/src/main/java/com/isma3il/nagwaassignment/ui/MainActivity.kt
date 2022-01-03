package com.isma3il.nagwaassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

import com.isma3il.nagwaassignment.AssignmentClass
import com.isma3il.nagwaassignment.R
import com.isma3il.nagwaassignment.databinding.ActivityMainBinding

import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //View Model
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject component to activity
        (application as AssignmentClass).applicationComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}