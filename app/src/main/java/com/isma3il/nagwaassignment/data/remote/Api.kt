package com.isma3il.nagwaassignment.data.remote

import com.isma3il.nagwaassignment.utils.Constants
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface Api {

    @Streaming
    @GET
    fun downloadFile(
        @Url url: String,
        @Header(Constants.DOWNLOAD_IDENTIFIER_HEADER) downloadIdentifier: String,
        @Header(Constants.DOWNLOAD_POSITION_HEADER) downloadPosition: Int
    ): Observable<Response<ResponseBody>>
}