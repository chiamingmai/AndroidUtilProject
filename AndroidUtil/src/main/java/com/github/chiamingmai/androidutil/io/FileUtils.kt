package com.github.chiamingmai.androidutil.io

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/** The utility tool for File operations. */
object FileUtils {
    /** Create a file in the App's private Picture directory. */
    @Throws(IOException::class)
    fun createFileInPictureFolder(context: Context, prefix: String, suffix: String): File? {
        // Create an image file name
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { storageDir ->
            File.createTempFile(
                prefix,
                ".$suffix",
                storageDir /* directory */
            )
        }
    }

    /** Create a file in the App's private Download directory. */
    @Throws(IOException::class)
    fun createFileInDownloadFolder(context: Context, prefix: String, suffix: String): File? {
        // Create an image file name
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.let { storageDir ->
            File.createTempFile(
                prefix,
                ".$suffix",
                storageDir /* directory */
            )
        }
    }

    /** Write the content Uri to the target file.
     *
     * @param context The context of the application.
     * @param inputUri the content Uri to be written.
     * @param targetFile the target file.
     */
    fun writeUriContentToFile(context: Context, inputUri: Uri, targetFile: File) {
        // Open an input stream from the Uri
        context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
            try {
                // Create an output stream to the destination file
                FileOutputStream(targetFile).use { outputStream ->
                    // Buffer for reading/writing data
                    val buffer = ByteArray(1024)
                    var read: Int

                    // Read from the input stream and write to the output stream
                    while (inputStream.read(buffer).also { read = it } > 0) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}