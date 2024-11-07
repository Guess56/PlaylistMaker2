package com.example.playlistmaker.playList.data.repository

import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.repository.PlayListRepository
import com.example.playlistmaker.player.presentation.viewModel.toTrackPlayListEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListRepositoryImpl(private val appDataBase: AppDataBase):PlayListRepository {
    override fun savePlayList(textNamePlayList: String, textDescription: String, filePath: String):PlayList {
        val list = PlayList(
            playListId = 0,
            namePlayList = textNamePlayList,
            description = textDescription,
            image = filePath,
            filePath = filePath,
            count = 0,
            trackId = arrayListOf(),
        )
        return list
    }

    override suspend fun addTrackToPlayList(track: TrackEntity) {
        appDataBase.playListTrackDao().insertPlayListTrack(track.toTrackPlayListEntity())
    }

    override suspend fun getTrackId(track: String):TrackEntity {
        return appDataBase.trackDao().getTrackById(track.toLong())
    }
}

