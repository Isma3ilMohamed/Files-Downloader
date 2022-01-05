package com.isma3il.nagwaassignment.domain.repositories

import androidx.lifecycle.Observer
import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.data.remote.interceptors.model.ProgressEvent
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import io.reactivex.rxjava3.core.Observable
import org.reactivestreams.Subscriber
import java.util.concurrent.Flow

interface FileRepository {

    fun getListOfFiles():Observable<AppResult<List<NagwaFile>>>
    fun downloadFile(file: NagwaFile?):Observable<AppResult<NagwaFile?>>
}