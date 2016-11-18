package com.mlsdev.mapsappsample;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mlsdev.mapsappsample.databinding.ActivityMainBinding;
import com.mlsdev.mapsappsample.placesuggestions.PlaceSuggestionsActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MainViewModel());
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


    public class MainViewModel {

        public void onPickPlaceButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, PlacePickerActivity.class));
        }

        public void onMapButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, BasicMapActivity.class));
        }

        public void onStaticImageButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, StaticImageMapActivity.class));
        }

        public void onPlaceSuggestionsButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, PlaceSuggestionsActivity.class));
        }

    }
}
