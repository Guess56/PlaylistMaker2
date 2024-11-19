package com.example.playlistmaker.playList.data.db.repository

import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.playList.data.converter.Converter
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.repository.PlayListDbRepository
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListDbRepositoryImpl(private val appDataBase: AppDataBase): PlayListDbRepository {
    override suspend fun insetPlayList(playList: List<PlayListEntity>) {
        appDataBase.playListDao().insertPlayList(playList)
    }

    override suspend fun insertTrackPlayList(track: PlayListTrackEntity) {
        appDataBase.playListTrackDao().insertPlayListTrack(track)
    }

    override suspend fun getPlayList(): Flow<List<PlayListEntity>> = flow {
        val playList = appDataBase.playListDao().getPlayList()
        emit(playList)
    }

    override suspend fun getList(): List<PlayListEntity> {
        val list = appDataBase.playListDao().getPlayList()
        return  list
    }

    override suspend fun getPlayListId(id:Int): Flow<PlayListEntity> = flow {
        val playList = appDataBase.playListDao().getPlayListId(id)
        emit(playList)
    }

    override suspend fun deletePlayList(playList: PlayListEntity) {
        appDataBase.playListDao().deletePlayList(playList)
    }

    override suspend fun getTrackPlayList(): Flow<List<PlayListTrackEntity>> = flow{
       val track = appDataBase.playListTrackDao().getPlayListTrackId()
        emit((track))
    }

    override suspend fun getTrackDp(): List<PlayListTrackEntity> {
        val track = appDataBase.playListTrackDao().getPlayListTrackId()
        return track
    }

    override suspend fun getTrack(id: Long): PlayListTrackEntity {
        val track = appDataBase.playListTrackDao().getTrackDb(id)
        return track
    }

    override suspend fun deleteTrackDb(track: PlayListTrackEntity) {
        appDataBase.playListTrackDao().deleteTrack(track)
    }


}