package com.example.geotrack.data.local

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class LocalImageStorageHandlerImpl(
    private val contentResolver: ContentResolver,
    private val context: Context
) : LocalImageStorageHandler {
    override suspend fun createImageFile(bitmap: Bitmap): String {
        val fileName = "${System.currentTimeMillis()}.jpg"
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appPrivateStorageDirectory)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }
        return fileName
    }

    override suspend fun readBitmapFromFilePath(path: String): Bitmap? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appPrivateStorageDirectory)
        Log.i("КАРТИНКА", filePath.path)
        val file = File(filePath, path)

        if (!file.exists()) {
            throw Exception("Image file not found at: ${file.absolutePath}")
        }

        return BitmapFactory.decodeFile(file.absolutePath)
    }

    override suspend fun deleteFileByFilePath(path: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appPrivateStorageDirectory)
        val file = File(filePath, path)
        if (file.exists()) {
            file.delete()
        }
    }

    companion object {
        const val appPrivateStorageDirectory = "geotrackStorage"
    }

}