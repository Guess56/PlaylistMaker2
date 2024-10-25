package com.example.playlistmaker.search.presentation.ViewModel


import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.search.domain.api.Consumer
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrackSearchViewModel(private val trackInteractor: TrackInteractor,private val historyInteractor: HistoryInteractor) : ViewModel() {


    private val getTrack = trackInteractor

    private val screenState = MutableLiveData<TrackSearchState>()
    private var textInput = ""
    private var searchJob: Job? = null


    fun getScreenState(): LiveData<TrackSearchState> = screenState

    private fun loadData( text: String) {
        screenState.value = TrackSearchState.Loading
        viewModelScope.launch {
            getTrack
                .searchTrack(text)
                .collect{ pair ->
                    processResult(pair.first,pair.second)
                }
        }
    }

    private fun processResult(text:List<Track>?,error : Int?){
        val track = mutableListOf<Track>()
        if (text != null) {
            track.addAll(text)
        }
        when{
           error != null -> {
               screenState.postValue(TrackSearchState.Error(error))
           }
            else -> {
                screenState.postValue(TrackSearchState.Content(track))
            }
        }
    }

    fun searchDebounce(changedText: String){
        if (textInput  == changedText) {
            return
        }
        textInput = changedText
        searchJob?.cancel()
        if (changedText.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                loadData(changedText)
            }
        }
    }
    fun refreshSearch(text: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadData(text)
        }
    }

    fun checkFavorite(){
        viewModelScope.launch {
            historyInteractor.getTrackFlow().collect{tracks->
                processHistoryResult(tracks)
            }
        }
    }
    private fun processHistoryResult(tracks: List<Track>) {
        renderState(TrackSearchState.ContentHistory(tracks))
    }
    private fun renderState(state: TrackSearchState) {
        screenState.postValue(state)
    }

    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}