package com.instabug.android_challenge.utils


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FilesUtils {

    fun convertInputStreamToFile(inputStream: InputStream?, outputFileName: String, outputFolder: File): File? {
        if (inputStream == null) {
            return null
        }

        val outputFile = File(outputFolder, outputFileName)

        try {
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.close()
            inputStream.close()

            return outputFile
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
            return null
        }
    }

    fun createFolder(dir: File):Boolean{
        if (!dir.exists()) {
            return dir.mkdir()
        }
        return false
    }

    fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var fileName = ""
        var cursor: Cursor? = null

        try {
            cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                fileName = it.getString(nameIndex)
            }
        } catch (e: Exception) {
            // Handle the exception
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return fileName
    }

}