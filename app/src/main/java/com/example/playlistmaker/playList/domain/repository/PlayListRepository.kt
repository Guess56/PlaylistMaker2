package com.example.playlistmaker.playList.domain.repository

import com.example.playlistmaker.playList.domain.db.model.PlayList

interface PlayListRepository {
    fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList
}