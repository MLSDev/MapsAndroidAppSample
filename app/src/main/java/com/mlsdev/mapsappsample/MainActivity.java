package com.mlsdev.mapsappsample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mlsdev.mapsappsample.databinding.ActivityMainBinding;
import com.mlsdev.mapsappsample.placesuggestions.PlaceSuggestionsActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnOpenMap.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BasicMapActivity.class)));
        binding.btnPickAPlace.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PlacePickerActivity.class)));
        binding.btnPlaceSuggestions.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PlaceSuggestionsActivity.class)));
        binding.btnStaticImage.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, StaticImageMapActivity.class)));

        getLocationPermission();
    }

    private void getLocationPermission() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (!granted)
                        MainActivity.this.finish();
                });
    }

}
