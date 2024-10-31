package com.example.playlistmaker.playList.presentation.playListViewModel

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

sealed interface PlayListState {
    data class Content(val data:List<PlayListEntity>):PlayListState
    data class Error(val message:String):PlayListState
}