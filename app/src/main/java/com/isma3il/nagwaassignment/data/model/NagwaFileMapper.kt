package com.isma3il.nagwaassignment.data.model

import com.isma3il.nagwaassignment.base.Mapper
import com.isma3il.nagwaassignment.domain.model.NagwaFile
import javax.inject.Inject

class NagwaFileMapper @Inject constructor() :Mapper<FileResponse.FileResponseItem,NagwaFile> {
    override fun map(input: FileResponse.FileResponseItem): NagwaFile {
        return NagwaFile(input.id,input.name,input.type,input.url)
    }
}