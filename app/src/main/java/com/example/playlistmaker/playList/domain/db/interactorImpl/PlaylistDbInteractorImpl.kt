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

    override suspend fun deletePlayList(playList: PlayListEntity) {
        return repository.deletePlayList(playList)
    }

}