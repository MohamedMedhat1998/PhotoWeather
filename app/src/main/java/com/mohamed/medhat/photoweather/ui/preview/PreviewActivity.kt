package com.mohamed.medhat.photoweather.ui.preview

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mohamed.medhat.photoweather.R
import com.mohamed.medhat.photoweather.databinding.ActivityPreviewBinding
import com.mohamed.medhat.photoweather.ui.BaseActivity
import com.mohamed.medhat.photoweather.ui.main.IMAGE_PATH
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "PreviewActivity"

/**
 * The activity where the user can preview the image to share.
 */
@AndroidEntryPoint
class PreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private val previewViewModel: PreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerObservers()
        if (savedInstanceState == null) {
            addWeatherBanner()
        }
    }

    /**
     * Subscribes to the observable fields in the [previewViewModel].
     */
    private fun registerObservers() {
        previewViewModel.openLocationRequest.observe(this) {
            if (it && previewViewModel.shouldOpenLocationSettings) {
                previewViewModel.shouldOpenLocationSettings = false
                showAlertDialog(
                    title = getString(R.string.turn_gps_on_title),
                    message = getString(R.string.turn_gps_on_message),
                    positiveButtonLabel = getString(R.string.turn_gps_on_ok),
                    negativeButtonLabel = getString(R.string.turn_gps_on_cancel),
                    onPositiveButtonClicked = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        locationResultLauncher.launch(intent)
                    }
                )
            }
        }
    }

    /**
     * Starts the process of adding weather banner to the image passed to this activity.
     */
    private fun addWeatherBanner() {
        withLocationPermission {
            withImagePath {
                previewViewModel.applyImageWeatherBanner(it)
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

    /**
     * Proceeds with the passed lambda if the image path coming from the previous activity is found.
     * @param onImagePathFound What to do with this path.
     */
    private fun withImagePath(onImagePathFound: (imagePath: String) -> Unit) {
        if (intent != null && intent.hasExtra(IMAGE_PATH)) {
            onImagePathFound.invoke(intent.getStringExtra(IMAGE_PATH)!!)
        } else {
            showToast(getString(R.string.null_image_path_message))
        }
    }

    /**
     * Location result callback. (onActivityResult) alternative.
     */
    private val locationResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (previewViewModel.isLocationEnabled()) {
                withImagePath { imagePath ->
                    previewViewModel.onLocationEnabled(imagePath)
                }
            } else {
                showToast(getString(R.string.gps_was_not_open_message))
            }
        }
}