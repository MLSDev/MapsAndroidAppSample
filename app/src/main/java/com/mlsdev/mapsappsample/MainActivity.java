package com.mlsdev.mapsappsample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mlsdev.mapsappsample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MainViewModel());
    }

    public class MainViewModel {

        public void onPickPlaceButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, PlacePickerActivity.class));
        }

        public void onMapButtonClick(View view) {
            startActivity(new Intent(MainActivity.this, BasicMapActivity.class));
        }

        public void onStaticImageButtonClick(View view) {
        }

    }
}
