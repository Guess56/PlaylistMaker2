package com.example.playlistmaker.playList.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface PlayListTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayListTrack(playListTrack: PlayListTrackEntity)

}