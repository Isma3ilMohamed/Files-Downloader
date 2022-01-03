package com.isma3il.nagwaassignment.domain.model

enum class NagwaFileType {
    PDF,
    VIDEO
}

fun String.convertStringToNagwaFileType(): NagwaFileType {
    return when (this) {
        "PDF" -> NagwaFileType.PDF
        "VIDEO" -> NagwaFileType.VIDEO
        else -> NagwaFileType.PDF
    }
}