package com.isma3il.nagwaassignment.domain.model

import java.io.File

data class NagwaFile(
    val id: Int,
    val name: String,
    val type: NagwaFileType,
    val url: String,

    //file state
    var isSelected:Boolean=false,
    var status: NagwaFileStatus=NagwaFileStatus.IDLE,
    var progressPercentage:Int=0,

    //file location
    var savedFile: File? = null,
    var filePosition:Int=0,

    //error state
    var retry:Int = 4,
    var error:String=""
)
