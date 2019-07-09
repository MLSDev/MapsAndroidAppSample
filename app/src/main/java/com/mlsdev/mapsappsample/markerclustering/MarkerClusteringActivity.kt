package com.mlsdev.mapsappsample.markerclustering

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mlsdev.mapsappsample.BaseActivity
import com.mlsdev.mapsappsample.R
import com.mlsdev.mapsappsample.databinding.ActivityMarkerClusteringBinding

class MarkerClusteringActivity : BaseActivity() {
    lateinit var binding: ActivityMarkerClusteringBinding
    lateinit var viewModel: MarkerClusteringViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_marker_clustering)
        viewModel = MarkerClusteringViewModel()
        binding.viewModel = viewModel
    }

}