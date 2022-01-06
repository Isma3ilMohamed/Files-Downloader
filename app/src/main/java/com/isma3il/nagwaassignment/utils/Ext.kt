package com.isma3il.nagwaassignment.utils

import android.content.res.AssetManager
import android.view.View
import android.webkit.MimeTypeMap
import androidx.cardview.widget.CardView
import com.google.gson.Gson
import java.io.File

// asset extension for reading file
fun AssetManager.readAssetsFile(fileName: String): String =
    open(fileName).bufferedReader().use { it.readText() }



//view extensions
fun CardView.setColor(res: Int) {
    this.setBackgroundResource(res)
}


fun View.secretB() {
    if (!this.isGoneB()) {
        this.visibility = View.GONE
    }
}


fun View.showB() {
    if (!this.isVisibleB()) {
        this.visibility = View.VISIBLE
    }
}
fun View.isVisibleB(): Boolean = visibility == View.VISIBLE

fun View.isGoneB(): Boolean = visibility == View.GONE

//file extension
fun getMimeType(file: File): String {
    val extension = getExtension(file.name) ?: ""
    return if (extension.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
        extension.substring(1)
    ).toString() else "application/octet-stream"

}

fun getExtension(uri: String?): String? {
    if (uri == null) {
        return null
    }

    val dot = uri.lastIndexOf(".")
    return if (dot >= 0) {
        uri.substring(dot)
    } else {
        // No extension.
        ""
    }
}