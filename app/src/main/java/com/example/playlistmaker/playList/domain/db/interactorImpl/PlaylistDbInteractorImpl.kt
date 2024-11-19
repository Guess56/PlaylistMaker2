package com.example.playlistmaker.playList.domain.db.interactorImpl

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.db.repository.PlayListDbRepository
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

class PlaylistDbInteractorImpl(val repository:PlayListDbRepository):PlayListDbInteractor {
    override suspend fun insetPlayList(playList: List<PlayListEntity>) {
        return repository.insetPlayList(playList)
    }

    override suspend fun insertTrackPlayList(track: PlayListTrackEntity) {
        return repository.insertTrackPlayList(track)
    }

    override suspend fun getPlayList(): Flow<List<PlayListEntity>> {
        return repository.getPlayList()
    }

    override suspend fun getPlayListId(id: Int): Flow<PlayListEntity> {
        return repository.getPlayListId(id)
    }

    override suspend fun deletePlayList(playList: PlayListEntity) {
        return repository.deletePlayList(playList)
    }

    override suspend fun getPlayListTrackId(): Flow<List<PlayListTrackEntity>> {
        return repository.getTrackPlayList()
    }

    override suspend fun getTrackDb(): List<PlayListTrackEntity> {
        return repository.getTrackDp()
    }

    override suspend fun getList(): List<PlayListEntity> {
        return repository.getList()
    }

    override suspend fun getTrack(id: Long): PlayListTrackEntity {
        return repository.getTrack(id)
    }

    override suspend fun deleteTrackDb(track: PlayListTrackEntity) {
        return repository.deleteTrackDb(track)
    }

}