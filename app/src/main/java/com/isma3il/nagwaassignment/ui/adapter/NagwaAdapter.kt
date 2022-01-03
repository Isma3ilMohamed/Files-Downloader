package com.isma3il.nagwaassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isma3il.nagwaassignment.R
import com.isma3il.nagwaassignment.databinding.ItemFileBinding
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.model.NagwaFileType

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

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class NagwaHolder(private val binding: ItemFileBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.chbSelect.setOnCheckedChangeListener { _, isChecked ->
                callback.onSelectFile(isChecked,dataset[adapterPosition],adapterPosition)
            }

        }


        fun bind(item:NagwaFile)= with(binding){
            when(item.type){
                NagwaFileType.PDF -> ivFileType.setImageResource(R.drawable.ic_pdf)
                NagwaFileType.VIDEO -> ivFileType.setImageResource(R.drawable.ic_video)
            }

            tvFileName.text=item.name
        }
    }

    interface NagwaCallback{
        fun onSelectFile(isSelected:Boolean,file: NagwaFile,position: Int)
        fun retry(file: NagwaFile,position: Int)
    }

}