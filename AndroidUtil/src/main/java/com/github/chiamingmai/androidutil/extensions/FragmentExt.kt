@file:JvmName("FragmentExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Intent
import android.widget.Toast
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
