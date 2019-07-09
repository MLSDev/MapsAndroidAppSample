package com.mlsdev.mapsappsample;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mlsdev.mapsappsample.databinding.ActivityPlacePickerBinding;

public class PlacePickerActivity extends GoogleApiClientActivity {
    private ActivityPlacePickerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_picker);
        binding.setViewModel(new PlacePickerViewModel());
        startPlacesPickerActivity();

        binding.btnPickAPlace.setOnClickListener(view -> startPlacesPickerActivity());
    }

    private void startPlacesPickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(intentBuilder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                binding.getViewModel().setData(place);
            }
        }
    }

    public class PlacePickerViewModel {
        public final ObservableField<String> placeName;
        public final ObservableField<String> placeAddress;

        public PlacePickerViewModel() {
            placeName = new ObservableField<>();
            placeAddress = new ObservableField<>();
        }

        public void setData(Place place) {
            placeName.set(place.getName().toString());
            placeAddress.set(place.getAddress().toString());
        }
    }
}
