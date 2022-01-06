package com.isma3il.nagwaassignment.data.remote.interceptors.model

/**
DownloadProgressListener for keep track on downloading currentProgress
 */

interface DownloadProgressListener {
    fun onProgressUpdate(
        downloadPosition: Int,
        bytesRead: Long,
        contentLength: Long,
        done: Boolean
    )
}