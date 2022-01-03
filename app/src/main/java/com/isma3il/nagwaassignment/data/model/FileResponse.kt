package com.isma3il.nagwaassignment.data.model

class FileResponse : ArrayList<FileResponse.FileResponseItem>(){
    data class FileResponseItem(
        var id: Int,
        var name: String,
        var type: String,
        var url: String
    )


}