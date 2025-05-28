@file:JvmName("ActivityExt")

package com.github.chiamingmai.androidutil.extensions

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 顯示浮動式訊息(短時間)
 * @param messageId 訊息 Resource id
 */
fun Activity.showShortToast(@StringRes messageId: Int) = showToast(messageId, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(短時間)
 * @param message 訊息
 */
fun Activity.showShortToast(message: CharSequence) = showToast(message, Toast.LENGTH_SHORT)

/**
 * 顯示浮動式訊息(長時間)
 * @param messageId 訊息 Resource id
 */
fun Activity.showLongToast(@StringRes messageId: Int) = showToast(messageId, Toast.LENGTH_LONG)

/**
 * 顯示浮動式訊息(長時間)
 * @param message 訊息
 */
fun Activity.showLongToast(message: CharSequence) = showToast(message, Toast.LENGTH_LONG)
