package com.mlsdev.mapsappsample.placesuggestions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mlsdev.mapsappsample.GoogleApiClientActivity;
import com.mlsdev.mapsappsample.R;
import com.mlsdev.mapsappsample.databinding.ActivityPlaceSuggestionsBinding;

public class PlaceSuggestionsActivity extends GoogleApiClientActivity {
    private ActivityPlaceSuggestionsBinding binding;
    private PlacesViewModel viewModel;
    private PlacesAdapter placesAdapter;
    private static final String TAG = "places";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_suggestions);
        viewModel = new PlacesViewModel(binding, googleApiClient);
        binding.setViewModel(viewModel);
        binding.etSearch.addTextChangedListener(new OnSearchTextChangedListener());

        placesAdapter = new PlacesAdapter(viewModel);
        binding.rvPlacesSuggestions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvPlacesSuggestions.setAdapter(placesAdapter);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    public class OnSearchTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            binding.btnClearSearchText.setVisibility(editable.length() > 0 ? View.VISIBLE : View.INVISIBLE);

            if (editable.length() == 0)
                placesAdapter.clearData();

            if (editable.length() > 2 && !viewModel.isItemSelected())
                viewModel.searchPlaces(editable.toString());

            binding.etSearch.setSelection(editable.length());
        }
    }
}
