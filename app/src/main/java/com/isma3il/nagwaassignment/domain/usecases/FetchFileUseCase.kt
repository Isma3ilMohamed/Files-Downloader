package com.isma3il.nagwaassignment.domain.usecases

import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.base.BaseUseCase
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.repositories.FileRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FetchFileUseCase @Inject constructor(private val repository: FileRepository) :
    BaseUseCase<Nothing, Observable<AppResult<List<NagwaFile>>>>() {
    override fun execute(input: Nothing?): Observable<AppResult<List<NagwaFile>>> {
        return repository.getListOfFiles()
    }

}