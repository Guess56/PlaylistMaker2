package com.example.playlistmaker.playList.domain.interactor

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.search.data.db.entity.TrackEntity

interface PlayListInteractor {
    fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList
    suspend fun addTrackToPlayList(track : TrackEntity)

    suspend fun  getTrackId(track:String):TrackEntity
    suspend fun deletePlayListTrack(playListId: List<String>, trackId: String,playListEntity: PlayListEntity)
}