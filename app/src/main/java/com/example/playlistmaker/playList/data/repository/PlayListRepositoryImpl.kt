package com.example.playlistmaker.playList.data.repository

import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.repository.PlayListRepository

class PlayListRepositoryImpl():PlayListRepository {
    override fun savePlayList(textNamePlayList: String, textDescription: String, filePath: String):PlayList {
        val list = PlayList(
            playListId = 0,
            namePlayList = textNamePlayList,
            description = textDescription,
            image = filePath,
            filePath = filePath,
            count = 0,
            trackId = "",
        )
        return list
    }
}

