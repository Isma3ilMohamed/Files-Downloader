package com.isma3il.nagwaassignment.ui

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.isma3il.nagwaassignment.AssignmentClass
import com.isma3il.nagwaassignment.BuildConfig
import com.isma3il.nagwaassignment.databinding.ActivityMainBinding
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.ui.adapter.NagwaAdapter
import com.isma3il.nagwaassignment.utils.getMimeType
import com.isma3il.nagwaassignment.utils.setAllOnClickListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.io.File
import javax.inject.Inject

import android.widget.Toast
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityMainBinding

    //View Model
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    //adapter
    private val nagwaAdapter by lazy {
        NagwaAdapter(object : NagwaAdapter.NagwaCallback {
            override fun onFileClickListener(file: NagwaFile) {
                if (file.savedFile == null) {
                    if (checkPermission()){
                        viewModel.downloadFile(file)
                    }else{
                        requestPermission {
                            viewModel.downloadFile(file)
                        }
                    }

                } else {
                    openFile(file.savedFile)
                }
            }

            override fun onSelectFile(isSelected: Boolean, file: NagwaFile, position: Int) {

            }

            override fun retry(file: NagwaFile, position: Int) {

            }

        })
    }

    private fun openFile(file: File?) {
        val fileUri = file?.let {
            FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider",
                it
            )
        }
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, file?.let { getMimeType(it) })
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject component to activity
        (application as AssignmentClass).applicationComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupController()
        setupObservables()

    }

    private fun setupController() {
        showLoading()
        binding.rvFiles.adapter = nagwaAdapter


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
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                .show()
        })

        //update progress status
        viewModel.subscribeOnDownloadingProgress()
        viewModel.progressLiveData.observe(this, Observer {
            nagwaAdapter.updateItemPercentage(it.downloadProgress, it.filePosition)
        })


        //downloaded files
        viewModel.downloadFileLiveData.observe(this, Observer {
            nagwaAdapter.updateItem(it)
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
                .withPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        if (Environment.isExternalStorageManager()) {
                            action.invoke()
                        }else{
                            permissionDeniedMessage()
                        }

                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        permissionDeniedMessage()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        request: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
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
                            permissionDeniedMessage()
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
    private fun permissionDeniedMessage() {
        Toast.makeText(
            this@MainActivity,
            "Allow permission for storage access!",
            Toast.LENGTH_SHORT
        ).show()
    }

}