package com.example.playlistmaker.playList.presentation.playListViewModel
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.player.presentation.state.PlayListTrackState

sealed interface PlayListTrackGetState {
    data class Content(val data:List<PlayListTrackEntity>):PlayListTrackGetState
    data class Error(val message:String):PlayListTrackGetState
}