package com.isma3il.nagwaassignment.ui.adapter

import com.isma3il.nagwaassignment.domain.model.NagwaFile
import java.io.File

interface FilesCallback {
    fun retry(file: NagwaFile)
    fun openFile(file: File)
    fun executeMsg(msg: String)
}