package com.example.playlistmaker.playList.domain.repository

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.search.data.db.entity.TrackEntity

interface PlayListRepository {
    fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String):PlayList

    suspend fun addTrackToPlayList(track : TrackEntity)
    suspend fun  getTrackId(track: String):TrackEntity
    suspend fun  deleteTrackId(playListIdTrack: List<String>,trackId:String, playList:PlayListEntity)
    suspend fun updatePlayList(playlist:PlayListEntity,name:String,description:String,image:String)
}