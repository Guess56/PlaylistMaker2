package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListState

sealed interface PlayListTrackState {
    data class Content(val data:List<PlayListTrackEntity>): PlayListTrackState
}