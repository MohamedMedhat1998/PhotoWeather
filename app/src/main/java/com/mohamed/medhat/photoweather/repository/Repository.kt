package com.mohamed.medhat.photoweather.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.mohamed.medhat.photoweather.model.HistoryItem
import com.mohamed.medhat.photoweather.model.SimpleWeatherData

/**
 * A super type that represents the single source of truth for the app to get the data from.
 */
interface Repository {
    /**
     * Retrieves the weather data of a specific location.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A [SimpleWeatherData] object holding weather data.
     */
    suspend fun getWeatherData(lat: Double, lon: Double): SimpleWeatherData?

    /**
     * Saves the image to the history list.
     * @param bitmap The image to save.
     * @param imagePath The path to save to.
     * @return `true` if the image was saved successfully, `false` otherwise.
     */
    suspend fun saveImageToHistory(bitmap: Bitmap, imagePath: String): Boolean

    /**
     * @return An observable [LiveData] containing a list of [HistoryItem]s.
     */
    fun getHistoryItems(): LiveData<List<HistoryItem>>
}