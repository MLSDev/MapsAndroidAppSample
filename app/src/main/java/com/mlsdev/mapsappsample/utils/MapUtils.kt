package com.mlsdev.mapsappsample.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import kotlin.math.*

object MapUtils {

    fun calculationByDistance(from: LatLng, to: LatLng): Double {
        val radius = 6371// radius of earth in Km
        val lat1 = from.latitude
        val lat2 = to.latitude
        val lon1 = from.longitude
        val lon2 = to.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec)

        return radius * c
    }

    fun calculationByDistanceInKm(from: LatLng, to: LatLng): Double =
            (calculationByDistance(from, to) * 100.0).roundToInt() / 100.0

}