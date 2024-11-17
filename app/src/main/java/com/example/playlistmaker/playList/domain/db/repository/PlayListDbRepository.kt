package com.example.playlistmaker.playList.domain.db.repository


import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface PlayListDbRepository {

    suspend fun insetPlayList(playList: List<PlayListEntity>)
    suspend fun insertTrackPlayList(track:PlayListTrackEntity)
    suspend fun getPlayList(): Flow<List<PlayListEntity>>
    suspend fun getPlayListId(id:Int):Flow<PlayListEntity>
    suspend fun deletePlayList(playList: PlayListEntity)

    suspend fun getTrackPlayList():Flow<List<PlayListTrackEntity>>
}