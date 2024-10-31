package com.example.playlistmaker.playList.domain.interactor

import com.example.playlistmaker.playList.domain.db.model.PlayList

interface PlayListInteractor {
    fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList
}