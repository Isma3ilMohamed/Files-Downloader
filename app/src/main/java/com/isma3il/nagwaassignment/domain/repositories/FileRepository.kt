package com.isma3il.nagwaassignment.domain.repositories

import com.isma3il.nagwaassignment.domain.model.NagwaFile

interface FileRepository {

    fun getListOfFiles():List<NagwaFile>
}