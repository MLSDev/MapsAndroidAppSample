package com.mlsdev.mapsappsample

import android.os.Bundle
import android.util.ArrayMap
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.mlsdev.mapsappsample.databinding.ActivityStaticImageMapBinding
import com.mlsdev.mapsappsample.listener.OnSeekBarListener
import com.mlsdev.mapsappsample.url.Constants.CENTER
import com.mlsdev.mapsappsample.url.Constants.KEY
import com.mlsdev.mapsappsample.url.Constants.MAP_TYPE
import com.mlsdev.mapsappsample.url.Constants.MARKERS
import com.mlsdev.mapsappsample.url.Constants.SCALE
import com.mlsdev.mapsappsample.url.Constants.SIZE
import com.mlsdev.mapsappsample.url.Constants.ZOOM
import com.mlsdev.mapsappsample.url.UrlBuilder


class StaticImageMapActivity : BaseActivity() {
    private var binding: ActivityStaticImageMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_static_image_map)
        binding!!.viewModel = ViewModel()
        showBackButton(true)
    }

    inner class ViewModel {
        val zoomProgress: ObservableField<String> = ObservableField("0")
        val scaleProgress: ObservableField<String> = ObservableField("0")

        init {
            binding!!.sbZoom.setOnSeekBarChangeListener(OnSeekBarListener(zoomProgress))
            binding!!.sbScale.setOnSeekBarChangeListener(OnSeekBarListener(scaleProgress))
        }

        fun onGetImageMapButtonClick() {
            getStaticImageMap()
        }

        private fun getStaticImageMap() {

            if (binding!!.etCenter.text!!.toString().isEmpty()) {
                showAlertDialog(null, "Center can\'t be blank!")
                return
            }

            val mapType = binding!!.spMapTypes.selectedItem as String

            val params = ArrayMap<String, String>()
            params[CENTER] = binding!!.etCenter.text!!.toString()
            params[ZOOM] = binding!!.sbZoom.progress.toString()
            params[SIZE] = "550x300"
            params[MAP_TYPE] = mapType
            params[SCALE] = binding!!.sbScale.progress.toString()
            if (binding!!.cbAddMarker.isChecked) {
                val markerPlace = binding!!.etCenter.text!!.toString()
                // markerPlace can be defined like a lat lon pair (45.523908,-122.669866)
                params[MARKERS] = "size:large|color:blue|label:A|$markerPlace"
            }
            params[KEY] = getString(R.string.google_app_api_key)
            Glide.with(this@StaticImageMapActivity)
                    .load(UrlBuilder.buildUrl(params))
                    .crossFade()
                    .into(binding!!.ivStaticMap)
        }

    }


}
