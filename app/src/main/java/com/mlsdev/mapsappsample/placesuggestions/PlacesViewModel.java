package com.mlsdev.mapsappsample.placesuggestions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesViewModel extends BaseObservable implements OnItemClickListener {
    private GoogleApiClient googleApiClient;
    private boolean isItemSelected;
    public final ObservableField<String> searchFieldValue;
    public MutableLiveData<List<String>> placesLiveData = new MutableLiveData<>();

    public PlacesViewModel(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        searchFieldValue = new ObservableField<>();
    }

    public void searchPlaces(String value) {
        isItemSelected = false;
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_STREET_ADDRESS)
                .build();

        Places.GeoDataApi.getAutocompletePredictions(googleApiClient, value, null, autocompleteFilter)
                .setResultCallback(new ResultCallbacks<AutocompletePredictionBuffer>() {
                    @Override
                    public void onSuccess(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                        List<String> placesFullTextList = new ArrayList<>(autocompletePredictions.getCount());
                        for (AutocompletePrediction prediction : autocompletePredictions)
                            placesFullTextList.add(prediction.getFullText(null).toString());

                        autocompletePredictions.release();

                        placesLiveData.postValue(placesFullTextList);
                    }

                    @Override
                    public void onFailure(Status status) {
                        Log.e("PLACES_API", "Error: Places.GeoDataApi.getAutocompletePredictions()");
                    }
                });
    }

    @Override
    public void onItemClick(String placeFullText) {
        isItemSelected = true;
        placesLiveData.postValue(Collections.emptyList());
        searchFieldValue.set(placeFullText);
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }
}
