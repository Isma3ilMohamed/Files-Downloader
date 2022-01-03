package com.isma3il.nagwaassignment.base

sealed class AppResult<T> {
    class Success<T>(val data: T) : AppResult<T>()
    class Failure<T>(val errorMessage: String, val errorCode:Int? = null) : AppResult<T>()
}
