@file:JvmName("IntentExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Parcelable> Intent.putParcelableList(key: String, list: List<T>) =
    putParcelableArrayListExtra(key, ArrayList<T>().apply { addAll(list) })

inline fun <reified T : Serializable> Intent.getSerializableObject(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getSerializableExtra(key, T::class.java)

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

inline fun <reified T : Parcelable> Intent.getParcelableObject(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelableExtra(key, T::class.java)

        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

inline fun <reified T : Parcelable> Intent.getParcelableList(key: String): List<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelableArrayListExtra(key, T::class.java)

        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }

inline fun <reified T : Parcelable> Intent.getParcelableArrayObject(key: String): Array<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelableArrayExtra(key, T::class.java)

        else -> @Suppress("DEPRECATION")
        getParcelableArrayExtra(key)?.mapNotNull { it as? T }?.toTypedArray()
    }
