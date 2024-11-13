package com.example.playlistmaker.playList.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: List<PlayListEntity>)
    @Query("SELECT * FROM play_list_entity")
    suspend fun getPlayList(): List<PlayListEntity>

    @Query("SELECT COUNT(trackId)  FROM play_list_entity ")
    suspend fun getCount():Int

    @Delete(entity = PlayListEntity::class)
    suspend fun deletePlayList(listEntity: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlayListEntity)
}