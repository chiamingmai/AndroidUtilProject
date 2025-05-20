@file:JvmName("UriExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface

/** 取得uri原始檔案的檔名 */
fun Uri.getOriginalFileName(context: Context): String {
    val nameWihExt = DocumentFile.fromSingleUri(context, this)?.name ?: ""
    val lastIndex = nameWihExt.lastIndexOf(".")
    return if (lastIndex == -1) nameWihExt
    else {
        nameWihExt.substring(0, lastIndex)
    }
}

/** 取得uri原始檔案的副檔名 */
fun Uri.getFileExtension(context: Context): String? {
    val mimeType = context.contentResolver.getType(this)
    //or val mimeType = DocumentFile.fromSingleUri(requireContext(), uri)?.type
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
}

/** 取得uri 檔案名稱及副檔名 */
fun Uri.getFileNameWithExtension(context: Context): String =
    DocumentFile.fromSingleUri(context, this)?.name ?: ""

/** 取得uri原始檔案大小 */
fun Uri.getFileSize(context: Context): Long {
    try {
        val descriptor = context.contentResolver.openFileDescriptor(this, "r")
        val size = descriptor?.statSize ?: 0L
        try {
            descriptor?.close()
        } catch (_: Exception) {
        }
        return size
    } catch (_: Exception) {
    }
    return 0L
}

/** 判斷Uri是否為圖片 */
fun Uri.isImage(context: Context): Boolean {
    val mimeType = context.contentResolver.getType(this)
    return mimeType?.startsWith("image/") == true
}

/** Uri的圖片轉為正方向 */
fun Uri.rotateImageToCorrectOrientation(context: Context): Bitmap? {
    if (this.isImage(context).not()) return null
    try {
        val bmp: Bitmap
        var rotationAngle = 0f
        context.contentResolver.openInputStream(this).use { stream ->
            bmp = BitmapFactory.decodeStream(stream)
        }

        context.contentResolver.openInputStream(this).use { stream ->
            if (stream == null) return null

            val exif = ExifInterface(stream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            // Determine the rotation angle from EXIF data
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotationAngle = 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> rotationAngle = 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> rotationAngle = 270f
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED ->
                    rotationAngle = 0f
            }

            // If rotation is needed, rotate the Bitmap
            if (rotationAngle != 0f) {
                val matrix = Matrix()
                matrix.postRotate(rotationAngle)
                return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            }
            return bmp
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}