package com.example.playlistmaker.search.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.TrackHistoryState
import com.example.playlistmaker.search.presentation.utils.SingleEventLiveData

class TrackHistoryViewModel(interactor: HistoryInteractor) : ViewModel() {

    private val getTrackHistoryList = interactor

    private val screenState = MutableLiveData<TrackHistoryState>()
    private val clickedTrackIdEvent = SingleEventLiveData<Int>()

    init {
        loadData()
    }

    fun getScreenState(): LiveData<TrackHistoryState> = screenState

    fun getClickedTrackId():LiveData<Int> = clickedTrackIdEvent

    private fun loadData() {
        screenState.value = TrackHistoryState.Loading

        val trackHistoryList = getTrackHistoryList.getTrack()

        screenState.value = TrackHistoryState.Content(trackHistoryList)
    }

    fun onTrackClick(trackId : Int) {
        clickedTrackIdEvent.value = trackId
    }

    fun clearHistory() {
        getTrackHistoryList.clearHistory()
    }

    fun checkHistory(track : Track) : List<Track>{
        return getTrackHistoryList.checkHistory(track)
    }

    fun getHistory(): List<Track>{
        return getTrackHistoryList.getTrack()
    }

}