package com.mlsdev.mapsappsample.placesuggestions;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.mlsdev.mapsappsample.databinding.ActivityPlaceSuggestionsBinding;

import java.util.ArrayList;
import java.util.List;

public class PlacesViewModel extends BaseObservable {
    private ActivityPlaceSuggestionsBinding binding;
    private GoogleApiClient googleApiClient;
    public final ObservableField<String> searchFieldValue;

    public PlacesViewModel(ActivityPlaceSuggestionsBinding binding, GoogleApiClient googleApiClient) {
        this.binding = binding;
        this.googleApiClient = googleApiClient;
        searchFieldValue = new ObservableField<>();
    }

    public void onClearSearchFieldButtonClick(View view) {
        binding.etSearch.setText("");
    }

    public void searchPlaces(String value) {
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

                        ((PlacesAdapter) binding.rvPlacesSuggestions.getAdapter()).setData(placesFullTextList);
                    }

                    @Override
                    public void onFailure(Status status) {
                        Log.e("PLACES_API", "Error: Places.GeoDataApi.getAutocompletePredictions()");
                    }
                });
    }
}
