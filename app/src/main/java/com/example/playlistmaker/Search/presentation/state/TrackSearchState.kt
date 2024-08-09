package com.example.playlistmaker.Search.presentation.state

import com.example.playlistmaker.Search.domain.models.Track

sealed interface TrackSearchState {
    object Loading : TrackSearchState
    data class Content(val data : List<Track>) : TrackSearchState
    data class ContentHistory(val data: List<Track>): TrackSearchState
    data class Error(val message : Int): TrackSearchState
}