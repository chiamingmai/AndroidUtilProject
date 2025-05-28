@file:JvmName("ViewModelExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

/** Context from AndroidViewModel */
val AndroidViewModel.context: Context get() = this.getApplication()

/** Resources from AndroidViewModel */
val AndroidViewModel.resources: Resources get() = context.resources

/**
 * Retrieve string from resource
 *
 * @param resId String resource id
 * @param formatArgs arguments
 */
fun AndroidViewModel.getString(@StringRes resId: Int, vararg formatArgs: Any): String =
    context.getString(resId, * formatArgs)

/** Get color int
 *
 * @param colorResId Color resource id
 */
fun AndroidViewModel.getColor(@ColorRes colorResId: Int): Int =
    ContextCompat.getColor(context, colorResId)
