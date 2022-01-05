package com.isma3il.nagwaassignment.domain.usecases


import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.base.BaseUseCase
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.repositories.FileRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(private val repository: FileRepository) :
    BaseUseCase<NagwaFile, Observable<AppResult<NagwaFile?>>>() {
    override fun execute(input: NagwaFile?): Observable<AppResult<NagwaFile?>> {
        return repository.downloadFile(input)
    }


}