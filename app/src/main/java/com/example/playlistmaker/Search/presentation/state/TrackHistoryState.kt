package com.example.playlistmaker.Search.presentation.state

import com.example.playlistmaker.Search.domain.models.Track

sealed interface TrackHistoryState {
    object Loading : TrackHistoryState
    data class Content (val data : List<Track>) : TrackHistoryState
}