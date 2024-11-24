package com.example.playlistmaker.playList.data.repository

import android.util.Log
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.repository.PlayListRepository
import com.example.playlistmaker.player.presentation.viewModel.toTrackPlayListEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListRepositoryImpl(private val appDataBase: AppDataBase):PlayListRepository {
    override fun savePlayList(
        textNamePlayList: String,
        textDescription: String,
        filePath: String
    ): PlayList {
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

    override suspend fun getTrackId(track: String): TrackEntity {
        return appDataBase.trackDao().getTrackById(track.toLong())
    }

    override suspend fun deleteTrackId(
        playListId: List<String>,
        trackId: String,
        playList: PlayListEntity
    ) {
        val list = createTracksFromJson(playList.trackId)
        list.remove(trackId)

        val current = list.filter { track -> track == trackId }
        if (current.isEmpty()) {
            appDataBase.playListDao().updatePlayList(
                playList.copy(
                    trackId = createJsonFromTracks(
                        list
                    ),
                    count = list.size
                )
            )
        }
    }

    override suspend fun updatePlayList(playlist:PlayListEntity,name:String,description:String,image:String) {
        appDataBase.playListDao().updatePlayList(
            playlist.copy(
                namePlayList = name,
                description = description,
                image = image,
                filePath = image))
    }

    fun createJsonFromTracks(tracks: List<String>): String {
        return Gson().toJson(tracks)
    }

    fun createTracksFromJson(json: String): ArrayList<String> {
        if (json == "") return ArrayList()
        val trackListType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, trackListType)
    }
}

