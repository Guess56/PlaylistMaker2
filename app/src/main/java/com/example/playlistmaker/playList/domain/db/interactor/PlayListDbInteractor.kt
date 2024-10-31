package com.example.playlistmaker.playList.domain.db.interactor

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow

interface PlayListDbInteractor {
    suspend fun insetPlayList(playList: List<PlayListEntity>)
    suspend fun getPlayList(): Flow<List<PlayListEntity>>
    suspend fun deletePlayList(playList: PlayListEntity)
}