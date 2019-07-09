package com.mlsdev.mapsappsample.placesuggestions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.mlsdev.mapsappsample.GoogleApiClientActivity
import com.mlsdev.mapsappsample.R
import com.mlsdev.mapsappsample.databinding.ActivityPlaceSuggestionsBinding

class PlaceSuggestionsActivity : GoogleApiClientActivity() {
    lateinit var binding: ActivityPlaceSuggestionsBinding
    lateinit var viewModel: PlacesViewModel
    lateinit var placesAdapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_suggestions)
        viewModel = PlacesViewModel(googleApiClient)
        binding.viewModel = viewModel
        binding.etSearch.addTextChangedListener(OnSearchTextChangedListener())

        placesAdapter = PlacesAdapter { viewModel.onPlaceSelected(it) }

        binding.rvPlacesSuggestions.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPlacesSuggestions.adapter = placesAdapter

        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

        binding.btnClearSearchText.setOnClickListener { binding.etSearch.setText("") }
    }

    inner class OnSearchTextChangedListener : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            binding.btnClearSearchText.visibility = if (editable.isNotEmpty()) View.VISIBLE else View.INVISIBLE

            if (editable.isEmpty())
                placesAdapter.clearData()

            if (editable.length > 2 && !viewModel.isItemSelected)
                viewModel.searchPlaces(editable.toString())

            binding.etSearch.setSelection(editable.length)
        }
    }

    companion object {
        private const val TAG = "places"
    }
}
