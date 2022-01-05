package com.isma3il.nagwaassignment.domain.model

enum class NagwaFileStatus {
    IDLE,
    WAITING,
    DOWNLOADING,
    DOWNLOADED,
    RETRY,
    ERROR
}