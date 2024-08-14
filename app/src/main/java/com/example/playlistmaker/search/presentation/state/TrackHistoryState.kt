package com.example.playlistmaker.search.presentation.state

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackHistoryState {
    object Loading : TrackHistoryState
    data class Content (val data : List<Track>) : TrackHistoryState
}