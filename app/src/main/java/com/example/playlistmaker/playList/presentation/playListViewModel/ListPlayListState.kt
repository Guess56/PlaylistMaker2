package com.example.playlistmaker.playList.presentation.playListViewModel

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

interface ListPlayListState {
    data class Content(val data: List<PlayListEntity>):ListPlayListState
    data class Error(val error:String):ListPlayListState
}