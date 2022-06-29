package com.mohamed.medhat.photoweather.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohamed.medhat.photoweather.model.HistoryItem

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryItem")
    fun getHistoryItems(): LiveData<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHistoryItem(historyItem: HistoryItem)
}