package com.isma3il.nagwaassignment.data.remote.interceptors

import com.isma3il.nagwaassignment.data.remote.interceptors.model.DownloadProgressListener
import com.isma3il.nagwaassignment.data.remote.interceptors.model.DownloadProgressResponseBody
import com.isma3il.nagwaassignment.data.remote.interceptors.model.ProgressEvent
import com.isma3il.nagwaassignment.utils.Constants
import com.isma3il.nagwaassignment.utils.EventBus

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject


class DownloadProgressInterceptor @Inject  constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        val builder: Response.Builder = originalResponse.newBuilder()


        val downloadIdentifier: String? = originalResponse.request.header(
            Constants.DOWNLOAD_IDENTIFIER_HEADER
        )
        val downloadPosition= originalResponse.request.header(
            Constants.DOWNLOAD_POSITION_HEADER
        )?.toInt()?:0




        val fileIdentifierIsSet = downloadIdentifier != null &&
                downloadIdentifier.isNotEmpty()

        val filePositionIsSet= downloadPosition!=-1


        if (filePositionIsSet && fileIdentifierIsSet) {
            //Here we could access file download currentProgress
            builder.body(
                DownloadProgressResponseBody(
                    downloadPosition,
                    originalResponse.body,
                    object : DownloadProgressListener {
                        override fun onProgressUpdate(
                            downloadPosition:Int,
                            bytesRead: Long,
                            contentLength: Long,
                            done: Boolean
                        ) {
                            // we post an event into the Bus !
                            if (!done){
                                EventBus.publish(ProgressEvent(
                                    downloadPosition,
                                    contentLength,
                                    bytesRead
                                ))
                            }
                        }
                    })
            )
        } else {

            builder.body(originalResponse.body)
        }
        return builder.build()
    }


}