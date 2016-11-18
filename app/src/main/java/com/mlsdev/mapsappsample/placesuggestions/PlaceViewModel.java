package com.mlsdev.mapsappsample.placesuggestions;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

public class PlaceViewModel {
    public final ObservableField<String> placeFullText;
    private OnItemClickListener onItemClickListener;

    public PlaceViewModel(OnItemClickListener onItemClickListener) {
        placeFullText = new ObservableField<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void onClickView(View view) {
        Log.d("PlacesItem", placeFullText.get());
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(placeFullText.get());
    }
}
