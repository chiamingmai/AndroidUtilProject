@file:JvmName("ViewExt")

package com.github.chiamingmai.androidutil.extensions

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

val RecyclerView.ViewHolder.context: Context get() = itemView.context