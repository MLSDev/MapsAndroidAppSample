package com.mlsdev.mapsappsample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mlsdev.mapsappsample.databinding.ActivityMainBinding
import com.mlsdev.mapsappsample.placesuggestions.PlaceSuggestionsActivity
import com.tbruyelle.rxpermissions.RxPermissions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.btnOpenMap.setOnClickListener {
            startActivity(Intent(this@MainActivity, BasicMapActivity::class.java))
        }

        binding.btnPickAPlace.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlacePickerActivity::class.java))
        }

        binding.btnPlaceSuggestions.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlaceSuggestionsActivity::class.java))
        }

        binding.btnStaticImage.setOnClickListener {
            startActivity(Intent(this@MainActivity, StaticImageMapActivity::class.java))
        }

        binding.buttonClusterMarkers.setOnClickListener {

        }

        getLocationPermission()
    }

    private fun getLocationPermission() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (!granted)
                        this@MainActivity.finish()
                }
    }

}
