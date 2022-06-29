package com.mohamed.medhat.photoweather.ui.preview

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.mohamed.medhat.photoweather.R
import com.mohamed.medhat.photoweather.databinding.ActivityPreviewBinding
import com.mohamed.medhat.photoweather.ui.BaseActivity
import com.mohamed.medhat.photoweather.ui.main.IMAGE_PATH
import com.mohamed.medhat.photoweather.utils.State.*
import dagger.hilt.android.AndroidEntryPoint


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
        initViews()
        registerObservers()
        if (savedInstanceState == null) {
            addWeatherBanner()
        }
    }

    /**
     * Initializes the UI.
     */
    private fun initViews() {
        binding.btnPreviewRetry.setOnClickListener {
            addWeatherBanner()
        }
        binding.btnPreviewShare.setOnClickListener {
            withImagePath {
                previewViewModel.shareImage(it)
            }
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
        previewViewModel.bitmap.observe(this) {
            Glide.with(this)
                .load(it)
                .into(binding.ivPreview)
        }
        previewViewModel.state.observe(this) {
            binding.tvPreviewError.text = it.error
            when (it.state) {
                STATE_NORMAL -> {
                    binding.apply {
                        ivPreview.visibility = View.VISIBLE
                        btnPreviewRetry.visibility = View.INVISIBLE
                        tvPreviewError.visibility = View.INVISIBLE
                        btnPreviewShare.visibility = View.VISIBLE
                        pbPreviewLoading.visibility = View.INVISIBLE
                    }
                }
                STATE_LOADING -> {
                    binding.apply {
                        ivPreview.visibility = View.INVISIBLE
                        btnPreviewRetry.visibility = View.INVISIBLE
                        tvPreviewError.visibility = View.INVISIBLE
                        btnPreviewShare.visibility = View.INVISIBLE
                        pbPreviewLoading.visibility = View.VISIBLE
                    }
                }
                STATE_ERROR -> {
                    binding.apply {
                        ivPreview.visibility = View.INVISIBLE
                        btnPreviewRetry.visibility = View.VISIBLE
                        tvPreviewError.visibility = View.VISIBLE
                        btnPreviewShare.visibility = View.INVISIBLE
                        pbPreviewLoading.visibility = View.INVISIBLE
                    }
                }
            }
        }
        previewViewModel.shareIntent.observe(this) {
            if (previewViewModel.canShareImage) {
                previewViewModel.canShareImage = false
                shareImage(it.first, it.second)
            }
        }
    }

    /**
     * Starts the process of adding weather banner to the image passed to this activity.
     */
    private fun addWeatherBanner() {
        withLocationPermission(onLocationPermissionGranted = {
            withImagePath {
                previewViewModel.applyImageWeatherBanner(it)
            }
        }, onLocationPermissionDenied = {
            previewViewModel.announceErrorState(getString(R.string.permission_denied_message))
        })
    }

    /**
     * Executes the passed lambda if the location permission is granted.
     * @param onLocationPermissionGranted The lambda to execute when the location permission is granted.
     * @param onLocationPermissionDenied The lambda to execute when the location permission is denied.
     */
    private fun withLocationPermission(
        onLocationPermissionGranted: () -> Unit,
        onLocationPermissionDenied: () -> Unit
    ) {
        requirePermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            arrayOf(getString(R.string.location_permission_friendly_name)),
            onPermissionsDenied = {
                onLocationPermissionDenied.invoke()
            },
            onPermissionsGranted = {
                onLocationPermissionGranted.invoke()
            }
        )
    }

    /**
     * Proceeds with the passed lambda if the image path coming from the previous activity is found.
     * @param onImagePathFound What to do with this path.
     */
    private fun withImagePath(onImagePathFound: (imagePath: String) -> Unit) {
        if (intent != null && intent.hasExtra(IMAGE_PATH)) {
            onImagePathFound.invoke(intent.getStringExtra(IMAGE_PATH)!!)
        } else {
            previewViewModel.announceErrorState(getString(R.string.null_image_path_message))
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
                previewViewModel.announceErrorState(getString(R.string.gps_was_not_open_message))
            }
        }
}