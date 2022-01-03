package com.isma3il.nagwaassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

import com.isma3il.nagwaassignment.AssignmentClass
import com.isma3il.nagwaassignment.databinding.ActivityMainBinding
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.ui.adapter.NagwaAdapter

import javax.inject.Inject

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
            override fun onSelectFile(isSelected: Boolean, file: NagwaFile, position: Int) {

            }

            override fun retry(file: NagwaFile, position: Int) {

            }

        })
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
        binding.rvFiles.adapter=nagwaAdapter
    }

    private fun setupObservables() {
        //fetch files
        viewModel.fetchFiles()
        viewModel.filesLiveData.observe(this, Observer {
            hideLoading()
            nagwaAdapter.addData(it)
        })

        //observe errors
        viewModel.errorMsgOnFetchingList.observe(this, Observer {
            Snackbar.make(binding.root,it, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    viewModel.fetchFiles()
                }
                .show()
        })

    }


    private fun showLoading()= with(binding){
        pbLoading.visibility= View.VISIBLE
        rvFiles.visibility=View.GONE
    }
    private fun hideLoading()= with(binding){
        pbLoading.visibility= View.GONE
        rvFiles.visibility=View.VISIBLE
    }
}