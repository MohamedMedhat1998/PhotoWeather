package com.mohamed.medhat.photoweather.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.mohamed.medhat.photoweather.local.HistoryDao
import com.mohamed.medhat.photoweather.model.HistoryItem
import com.mohamed.medhat.photoweather.model.SimpleWeatherData
import com.mohamed.medhat.photoweather.networking.WebApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * A [Repository] with real implementation that fetches real data.
 */
class MainRepository @Inject constructor(
    private val webApi: WebApi,
    private val historyDao: HistoryDao
) : Repository {
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

    @Suppress("BlockingMethodInNonBlockingContext") // Suppressed the warning as the issue was handled
    override suspend fun saveImageToHistory(bitmap: Bitmap, imagePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val photoFile = File(imagePath)
                val outputStream = FileOutputStream(photoFile)
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    historyDao.addHistoryItem(HistoryItem(imagePath))
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override fun getHistoryItems(): LiveData<List<HistoryItem>> {
        return historyDao.getHistoryItems()
    }
}