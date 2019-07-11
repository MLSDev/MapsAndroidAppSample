package com.mlsdev.mapsappsample.markerclustering

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem

data class MarkerItem(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lng")
        val lng: Double,
        @SerializedName("title")
        val markerTitle: String?,
        @SerializedName("snippet")
        val markerDescriptor: String?
) : ClusterItem {

    override fun getSnippet(): String = markerDescriptor ?: ""

    override fun getTitle(): String = markerTitle ?: ""

    override fun getPosition(): LatLng = LatLng(lat, lng)
}