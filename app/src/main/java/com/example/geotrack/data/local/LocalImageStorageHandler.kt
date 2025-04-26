package com.example.geotrack.data.local

import android.graphics.Bitmap
import android.net.Uri

interface LocalImageStorageHandler {
    suspend fun createImageFile(bitmap: Bitmap): String
    suspend fun readBitmapFromFilePath(path: String): Bitmap?
    suspend fun deleteFileByFilePath(path: String)
}
