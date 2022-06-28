package com.mohamed.medhat.photoweather.repository

import com.mohamed.medhat.photoweather.model.SimpleWeatherData
import com.mohamed.medhat.photoweather.networking.WebApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A [Repository] with real implementation that fetches real data.
 */
class MainRepository @Inject constructor(private val webApi: WebApi) : Repository {
    override suspend fun getWeatherData(lat: Double, lon: Double): SimpleWeatherData? {
        return withContext(Dispatchers.IO) {
            try {
                val response = webApi.getLocationWeather(lat, lon)
                if (response.isSuccessful) {
                    return@withContext response.body()
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}