package com.isma3il.nagwaassignment.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.isma3il.nagwaassignment.BuildConfig
import java.io.File

object Utils {

    @RequiresApi(Build.VERSION_CODES.R)
    fun Activity.accessAllFile(){
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        val uri: Uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun Activity.openFile(file: File?) {
        val fileUri = file?.let {
            FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider",
                it
            )
        }
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, file?.let { getMimeType(it) })
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}