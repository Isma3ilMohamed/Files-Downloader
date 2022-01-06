package com.isma3il.nagwaassignment.data.repositories

import android.content.res.AssetManager
import android.os.Environment
import com.google.gson.Gson
import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.data.model.FileResponse
import com.isma3il.nagwaassignment.data.model.FileMapper
import com.isma3il.nagwaassignment.data.remote.Api
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.model.NagwaFileStatus
import com.isma3il.nagwaassignment.domain.repositories.FileRepository
import com.isma3il.nagwaassignment.utils.readAssetsFile
import io.reactivex.rxjava3.core.Observable
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppFileRepository @Inject constructor(
    private val assetManager: AssetManager,
    private val gson: Gson,
    private val mapper: FileMapper,
    private val api: Api
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

    override fun downloadFile(file: NagwaFile?): Observable<AppResult<NagwaFile?>> {
        val url=file?.url?:""
        val identifier=url.substringAfterLast("/")
        val position=file?.filePosition?:0


       return api.downloadFile(url,identifier,position).flatMap { response ->
           try {

            if (response.isSuccessful){
                //initial file
                val currentFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile,
                    identifier
                )
                // delete old file if exists
                if (currentFile.exists()){
                    currentFile.delete()
                }

                //create new one
                currentFile.createNewFile()

                //write data on new file
                val body = response.body()
                if (body!=null){
                    val sink: BufferedSink = currentFile.sink().buffer()
                    sink.writeAll(body.source())
                    sink.close()
                }

                //return result with downloaded state
                return@flatMap Observable.just(AppResult.Success(file.also {
                    it?.savedFile=currentFile
                    it?.status=NagwaFileStatus.DOWNLOADED
                }))
            }else{
                var retry=file?.retry?:0

                if (retry>1){
                    //give user a chance to try again
                    return@flatMap Observable.just(AppResult.Success(file.also {
                        it?.retry = --retry
                        it?.status=NagwaFileStatus.RETRY
                    }))
                }else{
                    //user had enough chances and download failed
                    return@flatMap Observable.just(AppResult.Success(file.also {
                        it?.retry = 0
                        it?.status=NagwaFileStatus.ERROR
                        it?.error="The download failed"
                    }))
                }

            }
          }catch (e:Exception){
              return@flatMap Observable.just(AppResult.Failure(e.localizedMessage?:""))
          }
        }
    }
}