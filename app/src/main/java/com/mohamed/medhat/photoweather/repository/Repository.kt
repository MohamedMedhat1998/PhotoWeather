package com.mohamed.medhat.photoweather.repository

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
}