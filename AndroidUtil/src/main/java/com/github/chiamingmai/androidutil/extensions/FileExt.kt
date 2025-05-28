@file:JvmName("FileExt")

package com.github.chiamingmai.androidutil.extensions

import java.io.File
import java.io.FileInputStream
import java.io.IOException

/** 刪除底下所有檔案 */
fun File.deleteAllFiles() {
    try {
        if (isDirectory) {
            listFiles()?.forEach {
                try {
                    it.deleteRecursively()
                } catch (_: Exception) {
                }
            }
        }
        delete()
    } catch (_: Exception) {
    }
}

/** 取得檔案的前面幾個bytes */
private fun File.getTheBeginningBytes(size: Int): ByteArray? {
    val magicNumber = ByteArray(size)
    return try {
        FileInputStream(this).use { inputStream ->
            inputStream.read(magicNumber)
        }
        magicNumber
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/** 檔案是否是圖片
 *  支援圖檔格式: JPG, JPEG, PNG
 * */
fun File.isImageFile(): Boolean {
    // Maximum size for both PNG (8 bytes) and JPG (3 bytes)
    val header = getTheBeginningBytes(8) ?: return false

    val ext = this.extension.lowercase()

    return when (ext) {
        "jpg", "jpeg" -> {
            // Check for JPG (magic number starts with FF D8 FF)
            header.size >= 3 &&
                    header[0] == 0xFF.toByte() &&
                    header[1] == 0xD8.toByte() &&
                    header[2] == 0xFF.toByte()

        }

        "png" -> {
            // Check for PNG (magic number starts with 89 50 4E 47 0D 0A 1A 0A)
            header.size >= 8 &&
                    header[0] == 0x89.toByte() &&
                    header[1] == 0x50.toByte() &&
                    header[2] == 0x4E.toByte() &&
                    header[3] == 0x47.toByte() &&
                    header[4] == 0x0D.toByte() &&
                    header[5] == 0x0A.toByte() &&
                    header[6] == 0x1A.toByte() &&
                    header[7] == 0x0A.toByte()
        }

        else -> false
    }
}

/** 取得影片檔案副檔名(根據magic number)
 *  支援影片格式: MP4, M4V, MOV, 3GP, 3G2, MKV, AVI, FLV, WMV
 * */
fun File.getVideoExtensionByMagicNumber(): String? {
    val header = getTheBeginningBytes(12) ?: return null

    try {
        // MP4, M4V, MOV – via "ftyp"
        if (header.size >= 12 && String(header.copyOfRange(4, 8)) == "ftyp") {
            val brand = String(header.copyOfRange(8, 12)).lowercase()
            return when {
                brand.startsWith("mp4") || brand.startsWith("isom") -> "mp4"
                brand.startsWith("m4v") -> "m4v"
                brand.startsWith("qt  ") -> "mov"
                brand.startsWith("3gp") -> "3gp"
                brand.startsWith("3g2") -> "3g2"
                else -> "mp4" // default fallback for unknown ftyp
            }
        }

        // MKV or WebM
        if (header.copyOfRange(0, 4)
                .contentEquals(byteArrayOf(0x1A, 0x45, 0xDF.toByte(), 0xA3.toByte()))
        ) {
            return "mkv" // Could also be "webm" but needs deeper inspection
        }

        // AVI
        if (header.copyOfRange(0, 4).contentEquals("RIFF".toByteArray()) &&
            header.copyOfRange(8, 12).contentEquals("AVI ".toByteArray())
        ) {
            return "avi"
        }

        // FLV
        if (header.copyOfRange(0, 3).contentEquals("FLV".toByteArray())) {
            return "flv"
        }

        // WMV
        if (header.copyOfRange(0, 4).contentEquals(byteArrayOf(0x30, 0x26, 0xB2.toByte(), 0x75))) {
            return "wmv"
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/** 檔案是否是影片
 *  支援影片格式: MP4, M4V, MOV, 3GP, 3G2, MKV, AVI, FLV, WMV
 * */
fun File.isVideoFile(): Boolean =
    extension.lowercase() == getVideoExtensionByMagicNumber()