package com.isma3il.nagwaassignment.utils

import android.content.res.AssetManager
import com.google.gson.Gson

fun AssetManager.readAssetsFile(fileName: String): String =
    open(fileName).bufferedReader().use { it.readText() }

inline fun <reified R> String.fromJson() : R {
    return Gson().fromJson(this, R::class.java)
}

inline fun <reified R> R.toJson() : String {
    return Gson().toJson(this, R::class.java)
}
