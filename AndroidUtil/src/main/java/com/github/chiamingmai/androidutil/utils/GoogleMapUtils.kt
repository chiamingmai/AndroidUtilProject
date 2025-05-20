package com.github.chiamingmai.androidutil.utils

import android.location.Location
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

/** Google map utility */
object GoogleMapUtils {
    /** Check compass state, this would change the angle of the [compassView].
     *
     * @param googleMap The google map view
     * @param compassView The compass view
     */
    @JvmStatic
    fun checkCompassState(googleMap: GoogleMap?, compassView: View) {
        if (googleMap == null) return

        if (compassView.isVisible.not()) {
            //因為有動畫支援，故不能隱藏view
            compassView.isVisible = true
        }

        val bearing = googleMap.cameraPosition.bearing.toInt()
        if (bearing == 0 || bearing == 360) {
            compassView.animate().alpha(0f).setDuration(500).start()
        } else {
            //bearing 是相對於北方轉了多少度(順時針計算)，需360 - bearing
            val rotation = (360 - bearing).toFloat()
            compassView.animate().rotation(rotation).alpha(1f).setDuration(500).start()
        }
    }

    /** Calculate the center point
     *
     * @param points The list of coordinates.
     * @return A double array with a length of 2, the first value is latitude, the second value is longitude.
     */
    @JvmStatic
    fun calculateCentroid(points: List<LatLng>): DoubleArray? {
        if (points.isEmpty()) return null

        val size = points.size.toDouble()
        var latSum = 0.0
        var lngSum = 0.0
        points.forEach {
            latSum += it.latitude
            lngSum += it.longitude
        }

        return doubleArrayOf(latSum / size, lngSum / size)
    }

    /** 計算多邊形座標總面積，單位:平方公尺 */
    @JvmStatic
    fun computeArea(points: List<LatLng>): Double = SphericalUtil.computeArea(points)

    /** 計算多邊形座標總面積，單位:平方公尺 */
    @JvmStatic
    fun computeAreaFromLocation(locations: List<Location>): Double =
        computeArea(locations.map { LatLng(it.latitude, it.longitude) })

    /** 計算兩個座標的距離，單位:公尺 */
    @JvmStatic
    fun computeDistance(from: LatLng, to: LatLng): Double =
        SphericalUtil.computeDistanceBetween(from, to)

    /** 計算兩個座標的距離，單位:公尺 */
    @JvmStatic
    fun computeDistanceFromLocation(from: Location, to: Location): Double =
        computeDistance(LatLng(from.latitude, from.longitude), LatLng(to.latitude, to.longitude))
}