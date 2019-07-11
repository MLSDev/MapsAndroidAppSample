package com.mlsdev.mapsappsample.markerclustering

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.mlsdev.mapsappsample.BaseActivity
import com.mlsdev.mapsappsample.R
import com.mlsdev.mapsappsample.databinding.ActivityMarkerClusteringBinding
import com.mlsdev.mapsappsample.databinding.LayoutMarkersClusterBinding
import com.mlsdev.mapsappsample.utils.DrawableToBitmapDecoder
import kotlin.random.Random

class MarkerClusteringActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMarkerClusteringBinding
    lateinit var viewModel: MarkerClusteringViewModel
    lateinit var clusterManager: ClusterManager<MarkerItem>
    private var googleMap: GoogleMap? = null

    private val markers = arrayListOf(
            R.drawable.ic_marker_healthcare_center,
            R.drawable.ic_marker_laboratory,
            R.drawable.ic_marker_vaccination)

    private val random = Random(1989)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_marker_clustering)
        viewModel = MarkerClusteringViewModel()
        binding.viewModel = viewModel
        observeMarkers()
        setupMap()
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = googleMap ?: map
        googleMap?.let {
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))
            clusterManager = ClusterManager(this, it)
            clusterManager.renderer = PrimaryMarkerRenderer(it, clusterManager)
            it.setOnCameraIdleListener(clusterManager)

            setupMarkers()
        }
    }

    private fun setupMarkers() {
        val inputStream = resources.openRawResource(R.raw.radar_search)
        viewModel.prepareMarkers(inputStream)
    }

    private fun observeMarkers() {
        viewModel.markers.observe(this, Observer { markers ->
            clusterManager.addItems(markers)
        })
    }

    inner class PrimaryMarkerRenderer(map: GoogleMap, clusterManager: ClusterManager<MarkerItem>)
        : DefaultClusterRenderer<MarkerItem>(applicationContext, map, clusterManager) {
        private val iconGenerator = IconGenerator(applicationContext)
        private val clusterBinding = LayoutMarkersClusterBinding.inflate(layoutInflater)

        override fun onBeforeClusterItemRendered(item: MarkerItem?, markerOptions: MarkerOptions?) {
            val drawableRes = markers[random.nextInt(0, 3)]
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        override fun onBeforeClusterRendered(cluster: Cluster<MarkerItem>?, markerOptions: MarkerOptions?) {
            iconGenerator.setBackground(ContextCompat.getDrawable(applicationContext, android.R.color.transparent))
            iconGenerator.setContentView(clusterBinding.root)
            clusterBinding.markersCount.text = cluster?.size.toString()
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MarkerItem>?): Boolean {
            return cluster?.size ?: 0 > 1
        }

    }

}