package com.example.playlistmaker.playList.domain.interactorImp

import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.interactor.PlayListInteractor
import com.example.playlistmaker.playList.domain.repository.PlayListRepository
import com.example.playlistmaker.search.data.db.entity.TrackEntity

class PlayListInteractorImpl(private val repository:PlayListRepository):PlayListInteractor {
    override fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList {
       return repository.savePlayList(textNamePlayList,textDescription,filePath)
    }

    override suspend fun addTrackToPlayList(track: TrackEntity) {
       return repository.addTrackToPlayList(track)
    }

    override suspend fun getTrackId(track: String):TrackEntity {
        return repository.getTrackId(track)
    }
}