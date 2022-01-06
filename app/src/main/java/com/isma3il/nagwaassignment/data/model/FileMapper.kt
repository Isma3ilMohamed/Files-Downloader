package com.isma3il.nagwaassignment.data.model

import com.isma3il.nagwaassignment.base.Mapper
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import com.isma3il.nagwaassignment.domain.model.convertStringToNagwaFileType
import javax.inject.Inject

class FileMapper @Inject constructor() : Mapper<FileResponse.FileResponseItem, NagwaFile> {
    override fun map(input: FileResponse.FileResponseItem): NagwaFile {
        return NagwaFile(input.id, input.name, input.type.convertStringToNagwaFileType(), input.url)
    }
}