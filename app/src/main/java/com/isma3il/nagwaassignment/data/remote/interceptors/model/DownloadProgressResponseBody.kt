package com.isma3il.nagwaassignment.data.remote.interceptors.model

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
Custom response body for downloading file
 */
class DownloadProgressResponseBody(
    private val downloadPosition: Int,
    private val responseBody: ResponseBody?,
    private val progressListener: DownloadProgressListener?
) :
    ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0L
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = responseBody?.source()?.let { source(it).buffer() }
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                }
                responseBody?.let {
                    progressListener?.onProgressUpdate(
                        downloadPosition,
                        totalBytesRead,
                        it.contentLength(),
                        bytesRead == -1L
                    )
                }
                return bytesRead
            }
        }
    }
}

