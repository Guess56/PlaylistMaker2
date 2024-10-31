package com.example.playlistmaker.playList.data.db.repository

import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.repository.PlayListDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListDbRepositoryImpl(private val appDataBase: AppDataBase): PlayListDbRepository {
    override suspend fun insetPlayList(playList: List<PlayListEntity>) {
        appDataBase.playListDao().insertPlayList(playList)
    }

    override suspend fun getPlayList(): Flow<List<PlayListEntity>> = flow {
        val playList = appDataBase.playListDao().getPlayList()
        emit(playList)
    }

    override suspend fun deletePlayList(playList: PlayListEntity) {
        appDataBase.playListDao().deletePlayList(playList)
    }


}