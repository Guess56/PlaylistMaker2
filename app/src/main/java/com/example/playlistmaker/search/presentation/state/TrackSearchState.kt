package com.example.playlistmaker.search.presentation.state

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {
    object Loading : TrackSearchState
    data class Content(val data : List<Track>) : TrackSearchState
    data class ContentHistory(val data: List<Track>): TrackSearchState
    data class Error(val message : Int): TrackSearchState
}