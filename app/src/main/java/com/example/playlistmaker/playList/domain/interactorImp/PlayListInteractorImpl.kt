package com.example.playlistmaker.playList.domain.interactorImp

import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.interactor.PlayListInteractor
import com.example.playlistmaker.playList.domain.repository.PlayListRepository

class PlayListInteractorImpl(private val repository:PlayListRepository):PlayListInteractor {
    override fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList {
       return repository.savePlayList(textNamePlayList,textDescription,filePath)
    }

}