package com.isma3il.nagwaassignment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isma3il.nagwaassignment.base.AppResult
import com.isma3il.nagwaassignment.data.remote.interceptors.model.ProgressEvent
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.usecases.DownloadFileUseCase
import com.isma3il.nagwaassignment.domain.usecases.FetchFileUseCase
import com.isma3il.nagwaassignment.ui.model.FileProgressUi
import com.isma3il.nagwaassignment.utils.EventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val filesUseCase: FetchFileUseCase,
    private val downloadUseCase: DownloadFileUseCase
) : ViewModel() {


    //Error
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String>
        get() = _errorMsg

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
                        is AppResult.Failure -> {
                            _errorMsg.postValue(it.errorMessage)
                        }
                        is AppResult.Success -> {
                            it.data?.let {
                                _filesLiveData.postValue(it)
                            }

                        }
                    }
                },
                onError = {
                    _errorMsg.postValue(it.localizedMessage ?: "")
                }
            )
    }


    //Download file
    private val _downloadFileLiveData = MutableLiveData<NagwaFile>()
    val downloadFileLiveData: LiveData<NagwaFile>
        get() = _downloadFileLiveData

    fun downloadFile(file: NagwaFile) {
        downloadUseCase.execute(file)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when(it){
                        is AppResult.Failure -> {
                            _errorMsg.postValue(it.errorMessage)
                        }
                        is AppResult.Success -> {
                            it.data?.let { _downloadFileLiveData.postValue(it) }
                        }
                    }
                },
                onError = {

                }
            )
    }



    private val _progressLiveData = MutableLiveData<FileProgressUi>()
    val progressLiveData: LiveData<FileProgressUi>
        get()= _progressLiveData

    fun subscribeOnDownloadingProgress() {
        EventBus.listen(ProgressEvent::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.percentIsAvailable()){
                        _progressLiveData.postValue(FileProgressUi(it.currentPosition, it.progress))
                        Timber.i("Current progress => " + it.progress)
                    }

                },
                onComplete = {
                    Timber.i("Current progress => Complete")
                },
                onError = {
                    Timber.i("Current progress => " + it.localizedMessage)
                }
            )
    }


}