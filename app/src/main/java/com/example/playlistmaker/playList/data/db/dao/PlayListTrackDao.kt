package com.example.playlistmaker.playList.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface PlayListTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayListTrack(playListTrack: PlayListTrackEntity)

    @Query("SELECT * FROM track_play_list WHERE timeAdd ORDER BY timeAdd DESC")
    suspend fun getPlayListTrackId(): List<PlayListTrackEntity>

    @Query("SELECT * FROM track_play_list WHERE trackId=:id")
    suspend fun getTrackDb(id: Long): PlayListTrackEntity
    @Delete(entity = PlayListTrackEntity::class)
    suspend fun deleteTrack(playListTrack: PlayListTrackEntity)
}