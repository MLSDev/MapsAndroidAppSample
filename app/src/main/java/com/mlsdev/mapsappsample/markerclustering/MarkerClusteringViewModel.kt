package com.mlsdev.mapsappsample.markerclustering

import androidx.lifecycle.MutableLiveData
import java.io.InputStream

class MarkerClusteringViewModel {
    val markers = MutableLiveData<List<MarkerItem>>()

    fun prepareMarkers(inputStream: InputStream) {
        val markers = MarkerItemReader.read(inputStream)
        this.markers.postValue(markers)
    }

}