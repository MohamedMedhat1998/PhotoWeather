package com.mohamed.medhat.photoweather.ui.preview

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "PreviewViewModel"

/**
 * An mvvm [ViewModel] for the [PreviewActivity].
 */
@HiltViewModel
class PreviewViewModel @Inject constructor() : ViewModel() {

    /**
     * Adds a weather banner overlay to the image whose path is passed to this function.
     * @param imagePath The path of the image to add the banner to.
     */
    fun applyImageWeatherBanner(imagePath: String) {
        Log.d(TAG, "applyImageWeatherBanner: $imagePath")
    }

}