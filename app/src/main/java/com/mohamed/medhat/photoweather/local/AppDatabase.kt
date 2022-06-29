package com.mohamed.medhat.photoweather.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamed.medhat.photoweather.model.HistoryItem

/**
 * Represents the database of the app.
 */
@Database(entities = [HistoryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}