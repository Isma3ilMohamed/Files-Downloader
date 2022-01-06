package com.isma3il.nagwaassignment.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isma3il.nagwaassignment.R
import com.isma3il.nagwaassignment.databinding.ItemFileBinding
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.model.NagwaFileStatus
import com.isma3il.nagwaassignment.domain.model.NagwaFileType
import com.isma3il.nagwaassignment.utils.Constants
import com.isma3il.nagwaassignment.utils.secretB
import com.isma3il.nagwaassignment.utils.setColor
import com.isma3il.nagwaassignment.utils.showB
import java.io.File

class NagwaAdapter(private val callback: NagwaCallback) :
    RecyclerView.Adapter<NagwaAdapter.NagwaHolder>() {

    private val dataset: MutableList<NagwaFile> = mutableListOf()
    private var mIsExecuting = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NagwaHolder {
        return NagwaHolder(
            ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NagwaHolder, position: Int) {
        holder.bind(dataset[position])
    }

    fun addData(data: List<NagwaFile>) {
        dataset.clear()
        dataset.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun getSelectedData(): List<NagwaFile> {
        return dataset.filter { it.isSelected }
    }

    fun changeExecutingState(isExecuting: Boolean) {
        mIsExecuting = isExecuting
    }


    fun updateItem(item: NagwaFile) {
        dataset[item.filePosition] = item
        notifyItemChanged(item.filePosition)
    }

    fun updateItemPercentage(progress: Int, position: Int) {
        //this check to prevent unnecessary notify
        if (dataset[position].progressPercentage!=Constants.UNKNOWN_LENGTH){
            dataset[position].progressPercentage = progress
            dataset[position].status = NagwaFileStatus.DOWNLOADING
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class NagwaHolder(private val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.ivRetry.setOnClickListener {
                if (mIsExecuting.not()) {
                    callback.retry(dataset[adapterPosition])
                }else{
                    callback.executeMsg("can't make any selection during downloading operations")
                }

            }
            binding.root.setOnClickListener {
                if (mIsExecuting.not()) {
                    when (dataset[adapterPosition].status) {
                        NagwaFileStatus.IDLE -> {
                            dataset[adapterPosition].isSelected = dataset[adapterPosition].isSelected.not()
                            binding.root.setColor(if (dataset[adapterPosition].isSelected) R.color.colorSelect else R.color.white)
                        }
                        NagwaFileStatus.DOWNLOADED -> {
                            dataset[adapterPosition].savedFile?.let { it1 -> callback.openFile(it1) }
                        }
                        NagwaFileStatus.RETRY -> {
                            callback.retry(dataset[adapterPosition])
                        }
                    }

                } else {
                    callback.executeMsg("can't make any selection during downloading operations")
                }

            }

        }


        fun bind(item: NagwaFile) = with(binding) {
            when (item.type) {
                NagwaFileType.PDF -> ivFileType.setImageResource(R.drawable.ic_pdf)
                NagwaFileType.VIDEO -> ivFileType.setImageResource(R.drawable.ic_video)
            }

            when (item.status) {
                NagwaFileStatus.IDLE -> {
                    idleState()
                }
                NagwaFileStatus.WAITING -> {
                    waitingState()
                }
                NagwaFileStatus.DOWNLOADING -> {
                    downloadingState(item.progressPercentage)
                }
                NagwaFileStatus.DOWNLOADED -> {
                    downloadedState()
                }
                NagwaFileStatus.RETRY -> {
                    retryState(item.retry)
                }
                NagwaFileStatus.ERROR -> {
                    errorState(item.error)
                }
            }
            //add position to item
            item.filePosition = adapterPosition

            tvFileName.text = item.name

        }

        private fun idleState() = with(binding) {

            root.setColor(R.color.white)
        }

        private fun waitingState() = with(binding) {
            pbLoading.showB()
            tvWaitng.showB()
            tvWaitng.text="Waiting to download"
            root.setColor(R.color.white)


            grpDwonload.secretB()
            ivRetry.secretB()
            ivOpen.secretB()
        }

        @SuppressLint("SetTextI18n")
        private fun downloadingState(progress: Int) = with(binding) {
            //show currentProgress

            if (progress == Constants.UNKNOWN_LENGTH) {
                grpDwonload.secretB()
                pbLoading.showB()
                tvWaitng.showB()
                tvWaitng.text="Please wait..."
            } else {
                grpDwonload.showB()
                pbLoading.secretB()
                tvWaitng.secretB()
                progressBar.progress = progress
                tvPrecentage.text = "$progress%"
            }
            root.setColor(R.color.white)


            ivRetry.secretB()
            ivOpen.secretB()

        }

        private fun downloadedState() = with(binding) {
            //show open
            ivOpen.showB()
            root.setColor(R.color.colorFinished)
            //hide others
            pbLoading.secretB()
            tvWaitng.secretB()
            ivRetry.secretB()
            grpDwonload.secretB()

        }

        @SuppressLint("SetTextI18n")
        private fun retryState(retryNum: Int) = with(binding) {
            //show open
            ivRetry.showB()
            tvWaitng.text = "you have $retryNum attempt"
            tvWaitng.showB()
            root.setColor(R.color.colorError)
            //hide others
            pbLoading.secretB()
            ivOpen.secretB()
            grpDwonload.secretB()
        }

        private fun errorState(msg: String) = with(binding) {
            //show open
            tvWaitng.showB()
            tvWaitng.text = msg
            root.setColor(R.color.colorError)
            //hide others
            pbLoading.secretB()
            ivRetry.secretB()
            ivOpen.secretB()
            grpDwonload.secretB()
        }

    }

    interface NagwaCallback {
        fun retry(file: NagwaFile)
        fun openFile(file:File)
        fun executeMsg(msg: String)
    }

}