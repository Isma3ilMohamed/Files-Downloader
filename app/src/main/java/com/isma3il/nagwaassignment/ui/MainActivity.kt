package com.isma3il.nagwaassignment.ui

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isma3il.nagwaassignment.ApplicationClass
import com.isma3il.nagwaassignment.BuildConfig
import com.isma3il.nagwaassignment.databinding.ActivityMainBinding
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.ui.adapter.FilesAdapter
import com.isma3il.nagwaassignment.utils.getMimeType
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.io.File
import javax.inject.Inject

import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.isma3il.nagwaassignment.domain.model.NagwaFileStatus
import com.isma3il.nagwaassignment.ui.adapter.FilesCallback
import com.isma3il.nagwaassignment.utils.Utils.accessAllFile
import com.isma3il.nagwaassignment.utils.Utils.openFile
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityMainBinding

    //View Model
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    //adapter
    private val nagwaAdapter by lazy {
        FilesAdapter(object : FilesCallback {
            override fun retry(file: NagwaFile) {
                retryDownloading(file)
            }

            override fun openFile(file: File) {
                this@MainActivity.openFile(file)
            }

            override fun executeMsg(msg: String) {
                errorMsg(msg)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject component to activity
        (application as ApplicationClass).applicationComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupController()
        setupObservables()

    }

    private fun setupController()=with(binding) {
        showLoading()
        rvFiles.adapter = nagwaAdapter
        fabDownload.setOnClickListener {
            if (checkPermission()){
                startDownloadingList()
            }else{
                requestPermission { startDownloadingList() }
            }
        }
        rvFiles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    fabDownload.hide()
                }else{
                    fabDownload.show()
                }
            }
        })
    }

    private fun startDownloadingList(){
        if (nagwaAdapter.getSelectedData().isNotEmpty()){
            //1- remove selection ui & shift items to waiting state
            //2- start downloading
            val downloadedList= nagwaAdapter.getSelectedData()
            downloadedList.map {
                it.isSelected=false
                it.status=NagwaFileStatus.WAITING
            }
            downloadedList.forEach {
                nagwaAdapter.updateItem(it)
            }

            viewModel.downloadFiles(downloadedList)

        }else{
            errorMsg("you should select one file at least")
        }
    }

    private fun retryDownloading(nagwaFile: NagwaFile){
        //change state to waiting
        nagwaAdapter.updateItem(
            nagwaFile.also {
                it.status=NagwaFileStatus.WAITING
            }
        )
        viewModel.retry(nagwaFile)
    }

    private fun setupObservables() {
        //fetch files
        viewModel.fetchFiles()
        viewModel.filesLiveData.observe(this, Observer {
            hideLoading()
            nagwaAdapter.addData(it)
        })

        //observe errors
        viewModel.errorMsg.observe(this, Observer {
            Timber.wtf(it)
            errorMsg(it)
        })

        //onProgressUpdate currentProgress status
        viewModel.subscribeOnDownloadingProgress()
        viewModel.progressLiveData.observe(this, Observer {
            nagwaAdapter.updateItemPercentage(it.downloadProgress, it.filePosition)
        })


        //downloaded files
        viewModel.downloadFileLiveData.observe(this, Observer {
            nagwaAdapter.updateItem(it)
        })

        //observe executing
        viewModel.executeLiveData.observe(this, Observer {
            nagwaAdapter.changeExecutingState(it)
        })
    }


    private fun showLoading() = with(binding) {
        pbLoading.visibility = View.VISIBLE
        rvFiles.visibility = View.GONE
    }

    private fun hideLoading() = with(binding) {
        pbLoading.visibility = View.GONE
        rvFiles.visibility = View.VISIBLE
    }

    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun requestPermission(action: () -> Unit) {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report?.areAllPermissionsGranted() == true) {
                            action.invoke()
                        } else {
                            accessAllFile()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        } else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report?.areAllPermissionsGranted() == true) {
                            action.invoke()
                        } else {
                            errorMsg("Allow permission for storage access!")
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }

    }

    private fun errorMsg(msg:String) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }


}