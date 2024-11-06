package com.example.playlistmaker

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favorite.data.db.dao.FavoriteDao
import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.playList.data.db.dao.PlayListDao
import com.example.playlistmaker.playList.data.db.dao.PlayListTrackDao
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity

import com.example.playlistmaker.search.data.db.dao.TrackDao
import com.example.playlistmaker.search.data.db.entity.TrackEntity

@Database( version = 1, entities = [FavoriteEntity::class,TrackEntity::class, PlayListEntity::class,PlayListTrackEntity::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun trackDao():TrackDao

    abstract fun playListDao(): PlayListDao

    abstract fun playListTrackDao():PlayListTrackDao
}