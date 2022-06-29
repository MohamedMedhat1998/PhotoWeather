package com.mohamed.medhat.photoweather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryItem(@PrimaryKey val imagePath: String) {
    /**
     * @return The name of the image.
     */
    fun getImageName(): String {
        return imagePath.split('/').last().dropLast(4)
    }
}
