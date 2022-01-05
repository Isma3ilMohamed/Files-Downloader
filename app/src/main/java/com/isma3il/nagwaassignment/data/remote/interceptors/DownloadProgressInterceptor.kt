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

        val filePositionIsSet= downloadPosition!=0


        if (filePositionIsSet && fileIdentifierIsSet) {
            //Here we could access file download progress
            builder.body(
                DownloadProgressResponseBody(
                    downloadIdentifier?:"",
                    downloadPosition,
                    originalResponse.body,
                    object : DownloadProgressListener {
                        override fun update(
                            downloadIdentifier: String?,
                            downloadPosition:Int,
                            bytesRead: Long,
                            contentLength: Long,
                            done: Boolean
                        ) {
                            // we post an event into the Bus !
                            if (!done){
                                EventBus.publish(ProgressEvent(downloadIdentifier?:"",downloadPosition, contentLength, bytesRead))
                            }
                        }
                    })
            )
        } else { // do nothing if it's not a file with an identifier :)
            builder.body(originalResponse.body)
        }
        return builder.build()
    }


}