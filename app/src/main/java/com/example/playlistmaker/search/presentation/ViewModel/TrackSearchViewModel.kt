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
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrackSearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {


    private val getTrack = trackInteractor

    private val screenState = MutableLiveData<TrackSearchState>()
    private var textInput = ""
    private var searchJob: Job? = null


    fun getScreenState(): LiveData<TrackSearchState> = screenState

    private fun loadData( text: String) {
        screenState.value = TrackSearchState.Loading
        getTrack.searchTrack(text, object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {
                when(data){
                    is ConsumerData.Error ->{
                        screenState.postValue(TrackSearchState.Error(data.message))
                    }
                    is ConsumerData.Data->{
                        screenState.postValue(TrackSearchState.Content(data.value))
                    }
                }
            }
        })
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



    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}