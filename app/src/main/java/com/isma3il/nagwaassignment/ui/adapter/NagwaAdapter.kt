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
import com.isma3il.nagwaassignment.utils.secretB
import com.isma3il.nagwaassignment.utils.showB

class NagwaAdapter(private val callback:NagwaCallback):RecyclerView.Adapter<NagwaAdapter.NagwaHolder>() {

    private val dataset:MutableList<NagwaFile> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NagwaHolder {
        return NagwaHolder(
            ItemFileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: NagwaHolder, position: Int) {
        holder.bind(dataset[position])
    }

    fun addData(data:List<NagwaFile>){
        dataset.clear()
        dataset.addAll(data)
        notifyItemRangeInserted(0,data.size)
    }


    fun updateItem(item: NagwaFile){
        dataset[item.filePosition]=item
        notifyItemChanged(item.filePosition)
    }

    fun updateItemPercentage(progress: Int,position: Int){
        dataset[position].progressPercentage=progress
        dataset[position].status=NagwaFileStatus.DOWNLOADING
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class NagwaHolder(private val binding: ItemFileBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                callback.onFileClickListener(dataset[adapterPosition])
            }

            binding.chbSelect.setOnCheckedChangeListener { _, isChecked ->
                callback.onSelectFile(isChecked,dataset[adapterPosition],adapterPosition)
            }

            binding.ivRetry.setOnClickListener {
                callback.retry(dataset[adapterPosition],adapterPosition)
            }

        }


        fun bind(item:NagwaFile)= with(binding){
            when(item.type){
                NagwaFileType.PDF -> ivFileType.setImageResource(R.drawable.ic_pdf)
                NagwaFileType.VIDEO -> ivFileType.setImageResource(R.drawable.ic_video)
            }

            when(item.status){
                NagwaFileStatus.IDLE ->{
                    idleState()
                }
                NagwaFileStatus.WAITING ->{
                    waitingState()
                }
                NagwaFileStatus.DOWNLOADING ->{
                    downloadingState(item.progressPercentage)
                }
                NagwaFileStatus.DOWNLOADED ->{
                    downloadedState()
                }
                NagwaFileStatus.RETRY ->{
                    retryState()
                }
                NagwaFileStatus.ERROR ->{
                    errorState(item.error)
                }
            }
            //add position to item
            item.filePosition=adapterPosition

            tvFileName.text=item.name

        }

        private fun idleState()= with(binding){
            //chbSelect.showB()
        }
        private fun waitingState()= with(binding){
            chbSelect.secretB()
            tvWaitng.showB()
        }
        @SuppressLint("SetTextI18n")
        private fun downloadingState(progress:Int)= with(binding){
            //show progress
            grpDwonload.showB()
            progressBar.progress = progress
            tvPrecentage.text="$progress%"

            chbSelect.secretB()
            tvWaitng.secretB()
            ivRetry.secretB()
            ivOpen.secretB()

        }
        private fun downloadedState()= with(binding){
            //show open
            ivOpen.showB()
            //hide others
            chbSelect.secretB()
            tvWaitng.secretB()
            ivRetry.secretB()
            grpDwonload.secretB()

        }
        private fun retryState()= with(binding){
            //show open
            ivRetry.showB()
            //hide others
            chbSelect.secretB()
            tvWaitng.secretB()
            ivOpen.secretB()
            grpDwonload.secretB()
        }
        private fun errorState(msg:String)= with(binding){
            //show open
            tvWaitng.showB()
            tvWaitng.text=msg
            //hide others
            chbSelect.secretB()
            ivRetry.secretB()
            ivOpen.secretB()
            grpDwonload.secretB()
        }

    }

    interface NagwaCallback{
        fun onFileClickListener(file: NagwaFile)
        fun onSelectFile(isSelected:Boolean,file: NagwaFile,position: Int)
        fun retry(file: NagwaFile,position: Int)
    }

}