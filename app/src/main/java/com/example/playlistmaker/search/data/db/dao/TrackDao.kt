package com.example.playlistmaker.search.data.db.dao

import androidx.annotation.Nullable
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertTrack(track: List<TrackEntity>)

    @Query("SELECT * FROM track_table")
     suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId LIKE :trackID")
     suspend fun getTrackById(trackID:Long):TrackEntity

    @Delete (entity = TrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)

    @Update(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateTrackEntity(trackEntity: TrackEntity)
}