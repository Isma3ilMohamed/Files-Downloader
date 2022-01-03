package com.isma3il.nagwaassignment.data.repositories

import android.content.res.AssetManager
import com.google.gson.Gson
import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.data.model.FileResponse
import com.isma3il.nagwaassignment.data.model.NagwaFileMapper
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.repositories.FileRepository
import com.isma3il.nagwaassignment.utils.readAssetsFile
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppFileRepository @Inject constructor(
    private val assetManager: AssetManager,
    private val gson: Gson,
    private val mapper: NagwaFileMapper
) :
    FileRepository {
    override fun getListOfFiles(): Observable<AppResult<List<NagwaFile>>> {

        // read Gson file
        val response=gson.fromJson<FileResponse>(
            assetManager.readAssetsFile("getListOfFilesResponse.json"),
            FileResponse::class.java
        )
        // map response to domain model
        val result=response.map {
            mapper.map(it)
        }




        return Observable.just(AppResult.Success(result))

    }
}