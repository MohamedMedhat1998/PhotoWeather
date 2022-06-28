package com.mohamed.medhat.photoweather.networking

import com.mohamed.medhat.photoweather.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Appends the app id to each request.
 */
class WeatherAuthenticator : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl =
            chain.request().url.newBuilder().addQueryParameter("appid", Constants.APP_ID).build()
        return chain.proceed(chain.request().newBuilder().url(newUrl).build())
    }
}