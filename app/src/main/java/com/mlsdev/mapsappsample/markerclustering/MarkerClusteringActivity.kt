package com.mlsdev.mapsappsample.markerclustering

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
import com.mlsdev.mapsappsample.utils.MapUtils
import com.tbruyelle.rxpermissions.RxPermissions

class MarkerClusteringActivity :
        BaseActivity(),
        OnMapReadyCallback,
        LocationListener,
        ClusterManager.OnClusterClickListener<MarkerItem>,
        ClusterManager.OnClusterItemClickListener<MarkerItem> {

    companion object {
        const val MIN_CLUSTER_SIZE = 1
    }

    lateinit var binding: ActivityMarkerClusteringBinding
    lateinit var viewModel: MarkerClusteringViewModel
    lateinit var clusterManager: ClusterManager<MarkerItem>
    lateinit var markerRenderer: PrimaryMarkerRenderer
    private var googleMap: GoogleMap? = null
    private var lastLatLng: LatLng? = null

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
            getLocationPermission()
            setupMarkers()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        googleMap?.isMyLocationEnabled = true
                        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 1f, this)
                    }
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
            return cluster?.size ?: 0 > MIN_CLUSTER_SIZE
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
        val prevItem = markerRenderer.prevSelectedItem
        val prevMarker = markerRenderer.getMarker(prevItem)

        if (prevItem != null && prevMarker != null) {
            val drawableRes = markers[prevItem.type] ?: R.drawable.ic_marker
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        markerRenderer.getMarker(markerItem)?.let { marker ->
            markerRenderer.prevSelectedItem = markerItem
            val drawableRes = markersLarge[markerItem?.type] ?: R.drawable.ic_marker
            val bitmap = DrawableToBitmapDecoder.getBitmap(applicationContext, drawableRes)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }

        showMarkerInfoDialog(markerItem)

        return true
    }

    private fun showMarkerInfoDialog(markerItem: MarkerItem?) {
        val currentLatLng = lastLatLng

        if (currentLatLng != null && markerItem != null) {
            val markerLatLng = LatLng(markerItem.lat, markerItem.lng)
            val distance = MapUtils.calculationByDistanceInKm(currentLatLng, markerLatLng)
            val message = getString(R.string.template_marker_info, markerItem.type.toString(), distance.toString())

            AlertDialog.Builder(this)
                    .setTitle(R.string.label_marker_info)
                    .setMessage(message)
                    .setPositiveButton(R.string.label_close, null)
                    .create()
                    .show()
        }
    }

    override fun onLocationChanged(location: Location?) {
        location?.let { lastLatLng = LatLng(it.latitude, it.longitude) }
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, p1: Int, p2: Bundle?) {
    }
}