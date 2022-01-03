package com.isma3il.nagwaassignment.domain.model

data class NagwaFile(
    val id: Int,
    val name: String,
    val type: NagwaFileType,
    val url: String,
    var status: NagwaFileStatus=NagwaFileStatus.IDLE,
    var retry:Int = 3,
    var error:String=""
)
