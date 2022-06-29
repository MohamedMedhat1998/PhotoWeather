package com.mohamed.medhat.photoweather.ui.preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mohamed.medhat.photoweather.R
import com.mohamed.medhat.photoweather.di.MainRepo
import com.mohamed.medhat.photoweather.model.StateHolder
import com.mohamed.medhat.photoweather.repository.Repository
import com.mohamed.medhat.photoweather.ui.BaseViewModel
import com.mohamed.medhat.photoweather.utils.PhotoEditor
import com.mohamed.medhat.photoweather.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "PreviewViewModel"

/**
 * An mvvm [ViewModel] for the [PreviewActivity].
 */
@SuppressLint("StaticFieldLeak") // Suppressed as it is fine to use the application context in the ViewModel.
@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context,
    @MainRepo private val repository: Repository,
    private val photoEditor: PhotoEditor
) : BaseViewModel(context) {

    private val _openLocationRequest = MutableLiveData(false)
    val openLocationRequest: LiveData<Boolean> = _openLocationRequest

    // Introduced to prevent the activity from automatically open the location settings on each configuration change
    var shouldOpenLocationSettings = false

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    /**
     * Adds a weather banner overlay to the image whose path is passed to this function.
     * @param imagePath The path of the image to add the banner to.
     */
    fun applyImageWeatherBanner(imagePath: String) {
        if (isLocationEnabled()) {
            onLocationEnabled(imagePath)
        } else {
            announceErrorState(context.getString(R.string.gps_was_not_open_message))
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
    @SuppressLint("MissingPermission") // Suppressed as the location permission was granted before.
    fun onLocationEnabled(imagePath: String) {
        setState(StateHolder(State.STATE_LOADING))
        val getLocationTask =
            fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
        getLocationTask.addOnSuccessListener {
            if (it != null) {
                Log.d(TAG, "location: ${it.latitude},${it.longitude}")
                viewModelScope.launch {
                    val weatherData = repository.getWeatherData(it.latitude, it.longitude)
                    if (weatherData != null) {
                        val bmp = photoEditor.addWeatherBanner(weatherData, imagePath)
                        repository.saveImageToHistory(bmp, imagePath)
                        _bitmap.postValue(bmp)
                        setState(StateHolder(State.STATE_NORMAL))
                    } else {
                        announceErrorState(context.getString(R.string.weather_response_error))
                    }
                }
            } else {
                Log.e(TAG, "onLocationEnabled: location is null")
                announceErrorState(context.getString(R.string.null_location_message))
            }
        }
        getLocationTask.addOnFailureListener {
            it.printStackTrace()
            announceErrorState(context.getString(R.string.null_location_message))
        }
    }
}