package com.mlsdev.mapsappsample.markerclustering

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.mlsdev.mapsappsample.BaseActivity
import com.mlsdev.mapsappsample.R
import com.mlsdev.mapsappsample.databinding.ActivityMarkerClusteringBinding
import com.mlsdev.mapsappsample.databinding.LayoutMarkersClusterBinding
import com.mlsdev.mapsappsample.utils.DrawableToBitmapDecoder

class MarkerClusteringActivity :
        BaseActivity(),
        OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MarkerItem>,
        ClusterManager.OnClusterItemClickListener<MarkerItem> {

    lateinit var binding: ActivityMarkerClusteringBinding
    lateinit var viewModel: MarkerClusteringViewModel
    lateinit var clusterManager: ClusterManager<MarkerItem>
    lateinit var markerRenderer: PrimaryMarkerRenderer
    private var googleMap: GoogleMap? = null

    private val markers = hashMapOf(
            MarkerItem.Type.HEALTHCARE_CENTER to R.drawable.ic_marker_healthcare_center,
            MarkerItem.Type.LABORATORY to R.drawable.ic_marker_laboratory,
            MarkerItem.Type.VACCINATION to R.drawable.ic_marker_vaccination)

    private val markersLarge = hashMapOf(
            MarkerItem.Type.HEALTHCARE_CENTER to R.drawable.ic_marker_healthcare_center_large,
            MarkerItem.Type.LABORATORY to R.drawable.ic_marker_laboratory_large,
            MarkerItem.Type.VACCINATION to R.drawable.ic_marker_vaccination_large)

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
            markerRenderer = PrimaryMarkerRenderer(it, clusterManager)
            clusterManager.renderer = markerRenderer
            clusterManager.setOnClusterClickListener(this)
            clusterManager.setOnClusterItemClickListener(this)
            it.setOnCameraIdleListener(clusterManager)
            it.setOnMarkerClickListener(clusterManager)
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

        private val clusterIconGenerator = IconGenerator(applicationContext)
        private val clusterBinding = LayoutMarkersClusterBinding.inflate(layoutInflater)
        var prevSelectedMarker: Marker? = null
        var prevSelectedItem: MarkerItem? = null

        override fun onBeforeClusterItemRendered(item: MarkerItem, markerOptions: MarkerOptions) {
            val drawableRes = markers[item.type] ?: R.drawable.ic_marker
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        override fun onBeforeClusterRendered(cluster: Cluster<MarkerItem>?, markerOptions: MarkerOptions?) {
            clusterIconGenerator.setBackground(ContextCompat.getDrawable(applicationContext, android.R.color.transparent))
            clusterIconGenerator.setContentView(clusterBinding.root)
            clusterBinding.markersCount.text = cluster?.size.toString()
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(clusterIconGenerator.makeIcon()))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MarkerItem>?): Boolean {
            return cluster?.size ?: 0 > 1
        }

    }

    override fun onClusterClick(cluster: Cluster<MarkerItem>): Boolean {

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        // Get the LatLngBounds
        val bounds = builder.build()

        // Animate camera to the bounds
        try {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    override fun onClusterItemClick(markerItem: MarkerItem?): Boolean {

        val prevMarker = markerRenderer.prevSelectedMarker
        val prevItem = markerRenderer.prevSelectedItem

        if (prevItem != null && prevMarker != null) {
            val drawableRes = markers[prevItem.type] ?: R.drawable.ic_marker
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        markerRenderer.getMarker(markerItem)?.let { marker ->
            markerRenderer.prevSelectedMarker = marker
            markerRenderer.prevSelectedItem = markerItem
            val drawableRes = markersLarge[markerItem?.type] ?: R.drawable.ic_marker
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        Snackbar.make(binding.root, markerItem?.type.toString(), Snackbar.LENGTH_SHORT).show()

        return true
    }

}