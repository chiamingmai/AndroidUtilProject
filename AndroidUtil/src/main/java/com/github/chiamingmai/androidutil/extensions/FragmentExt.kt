@file:JvmName("FragmentExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.ACTION_SYSTEM_FALLBACK_PICK_IMAGES
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/** 顯示DialogFragment */
fun DialogFragment.show(fragmentManager: FragmentManager) =
    show(fragmentManager, this::class.java.name)

/** 是否能開啟Intent內容 */
fun Fragment.isIntentAvailable(intent: Intent): Boolean =
    requireContext().isIntentAvailable(intent)

/**
 * 顯示浮動式訊息(短時間)
 * @param messageId 訊息 Resource id
 */
fun Fragment.showShortToast(@StringRes messageId: Int) =
    requireContext().showToast(messageId, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(短時間)
 * @param message 訊息
 */
fun Fragment.showShortToast(message: CharSequence) =
    requireContext().showToast(message, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(長時間)
 * @param messageId 訊息 Resource id
 */
fun Fragment.showLongToast(@StringRes messageId: Int) =
    requireContext().showToast(messageId, Toast.LENGTH_LONG)

/**
 * 顯示浮動式訊息(長時間)
 * @param message 訊息
 */
fun Fragment.showLongToast(message: CharSequence) =
    requireContext().showToast(message, Toast.LENGTH_LONG)

fun Fragment.getColor(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(requireContext(), colorRes)

/** Check if this permission is granted or not. */
fun Fragment.isPermissionGranted(permission: String): Boolean =
    requireContext().isPermissionGranted(permission)

/**
 * Check if the current device has support for the photo picker by checking the running
 * Android version, the SDK extension version or the picker provided by
 * a system app implementing [ACTION_SYSTEM_FALLBACK_PICK_IMAGES].
 */
fun Fragment.isPhotoPickerAvailable(): Boolean =
    requireContext().isPhotoPickerAvailable()

/** Check if the device has at least one camera. */
fun Fragment.isAnyCameraAvailable(): Boolean = requireContext().isAnyCameraAvailable()

/** Check if the device has a front facing camera. */
fun Fragment.isFrontCameraAvailable(): Boolean = requireContext().isFrontCameraAvailable()

/** Check if the device has a camera facing away from the screen. */
fun Fragment.isBackCameraAvailable(): Boolean = requireContext().isBackCameraAvailable()
