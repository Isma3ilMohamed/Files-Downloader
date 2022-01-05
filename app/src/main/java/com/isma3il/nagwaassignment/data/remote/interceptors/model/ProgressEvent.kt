package com.isma3il.nagwaassignment.data.remote.interceptors.model

data class ProgressEvent(val identifier: String,val currentPosition:Int, val contentLength: Long, val bytesRead: Long){
    val progress: Int = (bytesRead / (contentLength / 100f)).toInt()
    fun percentIsAvailable(): Boolean {
        return contentLength > 0
    }
}