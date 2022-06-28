package com.mohamed.medhat.photoweather.networking

import com.mohamed.medhat.photoweather.model.SimpleWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contains functions that connects to the remote api to fetch the data from.
 */
interface WebApi {

    /**
     * Fetches the weather data of a specific location.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A [SimpleWeatherData] holding the returned data.
     */
    @GET("weather?units=metric")
    suspend fun getLocationWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<SimpleWeatherData>

}