package com.isma3il.nagwaassignment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.usecases.FetchFileUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val filesUseCase: FetchFileUseCase) : ViewModel() {



    //Error
    private val _errorMsgOnFetchingList = MutableLiveData<String>()
    val errorMsgOnFetchingList:LiveData<String>
        get() = _errorMsgOnFetchingList

    //Fetched List
    private val _filesLiveData = MutableLiveData<List<NagwaFile>>()
    val filesLiveData: LiveData<List<NagwaFile>>
        get() = _filesLiveData


    fun fetchFiles() {
        filesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it) {
                        is AppResult.Failure ->{
                            _errorMsgOnFetchingList.postValue(it.errorMessage)
                        }
                        is AppResult.Success ->{
                            it.data?.let {
                                _filesLiveData.postValue(it)
                            }

                        }
                    }
                },
                onError = {
                    _errorMsgOnFetchingList.postValue(it.localizedMessage?:"")
                }
            )
    }


}