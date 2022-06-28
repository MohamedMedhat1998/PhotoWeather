package com.mohamed.medhat.photoweather.ui.preview

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mohamed.medhat.photoweather.R
import com.mohamed.medhat.photoweather.databinding.ActivityPreviewBinding
import com.mohamed.medhat.photoweather.ui.BaseActivity
import com.mohamed.medhat.photoweather.ui.main.IMAGE_PATH

private const val TAG = "PreviewActivity"

/**
 * The activity where the user can preview the image to share.
 */
class PreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val previewViewModel: PreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            addWeatherBanner()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val t = fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
        t.addOnSuccessListener {
            Log.d(TAG, "onCreate: ${it.latitude}:${it.longitude}")
        }
        t.addOnFailureListener {
            it.printStackTrace()
        }
        t.addOnCanceledListener {
            Log.d(TAG, "onCreate: cancelled!")
        }
    }

    /**
     * Starts the process of adding weather banner to the image passed to this activity.
     */
    private fun addWeatherBanner() {
        withLocationPermission {
            if (intent != null && intent.hasExtra(IMAGE_PATH)) {
                previewViewModel.applyImageWeatherBanner(intent.getStringExtra(IMAGE_PATH)!!)
            }
        }
    }

    /**
     * Executes the passed lambda if the location permission is granted.
     * @param onLocationPermissionGranted The lambda to execute when the location permission is granted.
     */
    private fun withLocationPermission(onLocationPermissionGranted: () -> Unit) {
        requirePermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            arrayOf(getString(R.string.location_permission_friendly_name))
        ) {
            onLocationPermissionGranted.invoke()
        }
    }
}