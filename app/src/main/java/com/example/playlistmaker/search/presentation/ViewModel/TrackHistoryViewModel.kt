package com.example.playlistmaker.search.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.favorite.presentation.FavoriteState

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.TrackHistoryState
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import com.example.playlistmaker.search.presentation.utils.SingleEventLiveData
import kotlinx.coroutines.launch

class TrackHistoryViewModel(interactor: HistoryInteractor) : ViewModel() {

    private val getTrackHistoryList = interactor

    private val screenState = MutableLiveData<TrackHistoryState>()
    private val clickedTrackIdEvent = SingleEventLiveData<Int>()

    private val favoriteState = MutableLiveData<TrackSearchState>()
        fun getFavoriteState(): LiveData<TrackSearchState> = favoriteState


    fun getScreenState(): LiveData<TrackHistoryState> = screenState

    fun getClickedTrackId(): LiveData<Int> = clickedTrackIdEvent

     fun loadData() {
        screenState.value = TrackHistoryState.Loading

        val trackHistoryList = getTrackHistoryList.getTrack()

        screenState.value = TrackHistoryState.Content(trackHistoryList)
    }

    fun onTrackClick(trackId: Int) {
        clickedTrackIdEvent.value = trackId
    }

    fun clearHistory() {
        getTrackHistoryList.clearHistory()
    }

    fun checkHistory(track: Track): List<Track> {
        return getTrackHistoryList.checkHistory(track)
    }

    fun getHistory(): List<Track> {
        return getTrackHistoryList.getTrack()
    }

    fun saveHistory(tracks: List<Track>):List<Track>{
        return getTrackHistoryList.saveTrackHistory(tracks)
    }
}

