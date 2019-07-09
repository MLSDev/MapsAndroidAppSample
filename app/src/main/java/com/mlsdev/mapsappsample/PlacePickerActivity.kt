package com.mlsdev.mapsappsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField

import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.mlsdev.mapsappsample.databinding.ActivityPlacePickerBinding

class PlacePickerActivity : GoogleApiClientActivity() {
    private var binding: ActivityPlacePickerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_picker)
        binding!!.viewModel = PlacePickerViewModel()
        startPlacesPickerActivity()

        binding!!.btnPickAPlace.setOnClickListener { startPlacesPickerActivity() }
    }

    private fun startPlacesPickerActivity() {
        val intentBuilder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(intentBuilder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data!!)
                binding!!.viewModel?.setData(place)
            }
        }
    }

    inner class PlacePickerViewModel {
        val placeName: ObservableField<String> = ObservableField()
        val placeAddress: ObservableField<String> = ObservableField()

        fun setData(place: Place) {
            placeName.set(place.name.toString())
            placeAddress.set(place.address!!.toString())
        }
    }
}
