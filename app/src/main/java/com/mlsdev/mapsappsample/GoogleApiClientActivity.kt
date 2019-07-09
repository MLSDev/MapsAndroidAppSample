package com.mlsdev.mapsappsample

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places

abstract class GoogleApiClientActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildGoogleApiClient()
    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient?.disconnect()
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    private fun showErrorDialog(errorCode: Int) {

        // Get the error dialog from Google Play services
        val errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST)

        // If Google Play services can provide an error dialog
        errorDialog?.show()
    }

    override fun onConnected(bundle: Bundle?) {
        Log.d("myLogs", "onConnected()")
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d("myLogs", "onConnectionSuspended()")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("myLogs", "onConnectionFailed() \r\n Error: " + connectionResult.errorMessage!!)
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST)
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }

        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.errorCode)
        }
    }

    companion object {
        private const val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
        const val PLACE_PICKER_REQUEST = 1
    }
}
