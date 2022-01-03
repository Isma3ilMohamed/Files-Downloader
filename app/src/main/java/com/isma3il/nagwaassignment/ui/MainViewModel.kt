package com.isma3il.nagwaassignment.ui

import androidx.lifecycle.ViewModel
import com.isma3il.nagwaassignment.domain.usecases.FetchFileUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(private val filesUseCase: FetchFileUseCase):ViewModel() {

    fun printHelloWorld()="Hello World"




}