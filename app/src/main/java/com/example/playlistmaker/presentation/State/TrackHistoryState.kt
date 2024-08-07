package com.example.playlistmaker.presentation.State

import com.example.playlistmaker.domain.models.Track

sealed interface TrackHistoryState {
    object Loading : TrackHistoryState
    data class Content (val data : List<Track>) : TrackHistoryState
}