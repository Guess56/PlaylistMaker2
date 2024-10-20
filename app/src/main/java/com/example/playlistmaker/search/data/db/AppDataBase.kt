package com.example.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.search.data.db.dao.TrackDao
import com.example.playlistmaker.search.data.db.entity.TrackEntity

@Database( version = 1, entities = [TrackEntity::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}