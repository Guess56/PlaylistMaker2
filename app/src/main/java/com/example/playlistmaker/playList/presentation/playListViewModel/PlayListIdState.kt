package com.example.playlistmaker.playList.presentation.playListViewModel

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

interface PlayListIdState {
    data class Content(val data:PlayListEntity):PlayListIdState
    data class Error(val error:String):PlayListIdState

}