@file:JvmName("MapExt")

package com.github.chiamingmai.androidutil.extensions

import com.google.android.gms.maps.GoogleMap

/** Disable the "My location" and "Compass" UIs in Google map view */
fun GoogleMap.disableMyLocationAndCompassUI() {
    uiSettings.isCompassEnabled = false
    uiSettings.isMyLocationButtonEnabled = false
}