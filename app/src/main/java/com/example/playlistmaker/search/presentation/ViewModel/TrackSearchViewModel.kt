package com.example.playlistmaker.search.presentation.ViewModel


import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.search.domain.api.Consumer
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.presentation.state.TrackSearchState



class TrackSearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {


    //private val getTrack = Creator.provideTrackInteractor()
    private val getTrack = trackInteractor

    private val screenState = MutableLiveData<TrackSearchState>()
    private val handler = Handler(Looper.getMainLooper())
    private var textInput = ""


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
    fun searchDebounce(changedText: String) {
        if (textInput == changedText) {
            return
        }

        this.textInput = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { loadData(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }



    companion object{

        /*fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackSearchViewModel()
            }
}*/

            private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}