@file:JvmName("ContextExt")

package com.github.chiamingmai.androidutil.extensions

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.ACTION_SYSTEM_FALLBACK_PICK_IMAGES
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.github.chiamingmai.androidutil.io.deleteAllFiles
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import java.io.File

/** 網路是否可用 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.let { cap ->
            when {
                cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    true
                }

                cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    true
                }

                cap.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    true
                }

                else -> false
            }
        } ?: false
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo?.isConnected == true
    }
}

/** Check if this permission is granted or not. */
fun Context.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/** 刪除檔案 */
fun Context.deleteFile(uri: Uri?) {
    if (uri == null) return
    try {
        //刪除檔案
        val documentFile = DocumentFile.fromSingleUri(this, uri)
        if (documentFile?.exists() == true)
            documentFile.delete()
    } catch (_: Exception) {
    }
    try {
        //再次確認刪除
        val f = File(uri.path!!)
        if (f.exists()) f.deleteAllFiles()
    } catch (_: Exception) {
    }
}

/** 是否能開啟Intent內容 */
fun Context.isIntentAvailable(intent: Intent): Boolean =
    try {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            PackageManager.MATCH_DEFAULT_ONLY or PackageManager.MATCH_SYSTEM_ONLY
        else PackageManager.MATCH_DEFAULT_ONLY

        intent.resolveActivityInfo(packageManager, flag) != null
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

/** 取得 Notification Builder */
fun Context.getNotificationBuilder(channelId: String): Notification.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        Notification.Builder(this, channelId)
    else Notification.Builder(this)
}

/**
 * 顯示浮動式訊息
 * @param messageId 訊息 Resource id
 * @param duration 時間長度
 */
fun Context.showToast(@StringRes messageId: Int, @Duration duration: Int) =
    Toast.makeText(this, messageId, duration).show()

/**
 * 顯示浮動式訊息
 * @param message 訊息
 * @param duration 時間長度
 */
fun Context.showToast(message: CharSequence, @Duration duration: Int) =
    Toast.makeText(this, message, duration).show()

/**
 * 顯示浮動式訊息(短時間)
 * @param messageId 訊息 Resource id
 */
fun Context.showShortToast(@StringRes messageId: Int) = showToast(messageId, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(短時間)
 * @param message 訊息
 */
fun Context.showShortToast(message: CharSequence) = showToast(message, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(長時間)
 * @param messageId 訊息 Resource id
 */
fun Context.showLongToast(@StringRes messageId: Int) = showToast(messageId, Toast.LENGTH_LONG)

/**
 * 顯示浮動式訊息(長時間)
 * @param message 訊息
 */
fun Context.showLongToast(message: CharSequence) = showToast(message, Toast.LENGTH_LONG)

/**
 * Check if the current device has support for the photo picker by checking the running
 * Android version, the SDK extension version or the picker provided by
 * a system app implementing [ACTION_SYSTEM_FALLBACK_PICK_IMAGES].
 */
fun Context.isPhotoPickerAvailable(): Boolean =
    ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this) ||
            isIntentAvailable(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                this.type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
            })

/** Check if the device has at least one camera. */
fun Context.isAnyCameraAvailable(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

/** Check if the device has a front facing camera. */
fun Context.isFrontCameraAvailable(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
//isCameraAvailableInternal(CameraCharacteristics.LENS_FACING_FRONT)

/** Check if the device has a camera facing away from the screen. */
fun Context.isBackCameraAvailable(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
//isCameraAvailableInternal(CameraCharacteristics.LENS_FACING_BACK)

private fun Context.isCameraAvailableInternal(lensFacing: Int): Boolean {
    val cameraManager: CameraManager =
        getSystemService(Context.CAMERA_SERVICE) as? CameraManager ?: return false
    try {
        cameraManager.cameraIdList.forEach { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == lensFacing) {
                return true
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}
