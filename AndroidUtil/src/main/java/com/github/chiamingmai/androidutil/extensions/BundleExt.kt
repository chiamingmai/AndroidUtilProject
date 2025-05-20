@file:JvmName("BundleExt")

package com.github.chiamingmai.androidutil.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Parcelable> Bundle.putParcelableList(key: String, list: List<T>) =
    this.putParcelableArrayList(key, ArrayList<T>().apply { addAll(list) })

inline fun <reified T : Serializable> Bundle.getSerializableObject(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getSerializable(key, T::class.java)

        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

inline fun <reified T : Parcelable> Bundle.getParcelableObject(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelable(key, T::class.java)

        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

inline fun <reified T : Parcelable> Bundle.getParcelableList(key: String): List<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelableArrayList(key, T::class.java)

        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

inline fun <reified T : Parcelable> Bundle.getParcelableArrayObject(key: String): Array<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelableArray(key, T::class.java)

        else -> @Suppress("DEPRECATION")
        getParcelableArray(key)?.mapNotNull { it as? T }?.toTypedArray()
    }
