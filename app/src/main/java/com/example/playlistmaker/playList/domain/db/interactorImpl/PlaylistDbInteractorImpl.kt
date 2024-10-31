package com.example.playlistmaker.playList.domain.db.interactorImpl

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.db.repository.PlayListDbRepository
import kotlinx.coroutines.flow.Flow

class PlaylistDbInteractorImpl(val repository:PlayListDbRepository):PlayListDbInteractor {
    override suspend fun insetPlayList(playList: List<PlayListEntity>) {
        return repository.insetPlayList(playList)
    }

    override suspend fun getPlayList(): Flow<List<PlayListEntity>> {
        return repository.getPlayList()
    }

    override suspend fun deletePlayList(playList: PlayListEntity) {
        return repository.deletePlayList(playList)
    }

}