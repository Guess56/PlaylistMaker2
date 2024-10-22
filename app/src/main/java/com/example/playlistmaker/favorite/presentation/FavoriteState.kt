package com.example.playlistmaker.favorite.presentation

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.TrackSearchState

sealed interface FavoriteState {

    data class Content(val data : List<Track>) : FavoriteState
    data class Error(val message : String): FavoriteState
}