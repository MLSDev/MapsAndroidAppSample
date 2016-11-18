package com.mlsdev.mapsappsample.placesuggestions;

import android.databinding.ObservableField;

public class PlaceViewModel {
    public final ObservableField<String> placeFullText;

    public PlaceViewModel() {
        placeFullText = new ObservableField<>();
    }
}
