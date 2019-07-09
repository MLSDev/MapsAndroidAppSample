package com.mlsdev.mapsappsample.placesuggestions

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import java.util.*

class PlacesViewModel(private val googleApiClient: GoogleApiClient?) : BaseObservable() {
    var isItemSelected: Boolean = false
    val searchFieldValue: ObservableField<String> = ObservableField()
    var placesLiveData = MutableLiveData<List<String>>()

    fun searchPlaces(value: String) {
        isItemSelected = false
        val autocompleteFilter = AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_STREET_ADDRESS)
                .build()

        if (googleApiClient == null)
            return

        Places.GeoDataApi.getAutocompletePredictions(googleApiClient, value, null, autocompleteFilter)
                .setResultCallback(object : ResultCallbacks<AutocompletePredictionBuffer>() {
                    override fun onSuccess(autocompletePredictions: AutocompletePredictionBuffer) {
                        val placesFullTextList = ArrayList<String>(autocompletePredictions.count)
                        for (prediction in autocompletePredictions)
                            placesFullTextList.add(prediction.getFullText(null).toString())

                        autocompletePredictions.release()

                        placesLiveData.postValue(placesFullTextList)
                    }

                    override fun onFailure(status: Status) {
                        Log.e("PLACES_API", "Error: Places.GeoDataApi.getAutocompletePredictions()")
                    }
                })
    }



    fun onPlaceSelected(placeFullText: String) {
        isItemSelected = true
        placesLiveData.postValue(emptyList())
        searchFieldValue.set(placeFullText)
    }
}
