package com.isma3il.nagwaassignment.domain.repositories

import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import io.reactivex.rxjava3.core.Observable

interface FileRepository {

    fun getListOfFiles():Observable<AppResult<List<NagwaFile>>>
}