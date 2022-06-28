package com.mohamed.medhat.photoweather.ui.preview

import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "PreviewViewModel"

/**
 * An mvvm [ViewModel] for the [PreviewActivity].
 */
@HiltViewModel
class PreviewViewModel @Inject constructor(private val locationManager: LocationManager) :
    ViewModel() {

    private val _openLocationRequest = MutableLiveData(false)
    val openLocationRequest: LiveData<Boolean> = _openLocationRequest

    // Introduced to prevent the activity from automatically open the location settings on each configuration change
    var shouldOpenLocationSettings = false

    /**
     * Adds a weather banner overlay to the image whose path is passed to this function.
     * @param imagePath The path of the image to add the banner to.
     */
    fun applyImageWeatherBanner(imagePath: String) {
        Log.d(
            TAG,
            "applyImageWeatherBanner: ${locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)}"
        )
        if (isLocationEnabled()) {
            onLocationEnabled(imagePath)
        } else {
            shouldOpenLocationSettings = true
            _openLocationRequest.value = true
        }
    }

    /**
     * @return `true` if the gps is on, `false` otherwise.
     */
    fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Gets executed when the location is enabled. It continues the process of applying the weather banner.
     * @param imagePath The path of the image to add the banner to.
     */
    fun onLocationEnabled(imagePath: String) {
        // TODO get the location
    }
}