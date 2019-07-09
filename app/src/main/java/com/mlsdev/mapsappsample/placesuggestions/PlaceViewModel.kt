package com.mlsdev.mapsappsample.placesuggestions

import android.util.Log
import android.view.View

import androidx.databinding.ObservableField

class PlaceViewModel(private val onItemClickListener: (place: String) -> Unit) {
    val placeFullText: ObservableField<String> = ObservableField()

    fun onClickView(view: View) {
        Log.d("PlacesItem", placeFullText.get())
        onItemClickListener(placeFullText.get()!!)
    }
}
