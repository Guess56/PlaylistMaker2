package com.example.playlistmaker.favorite.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.favorite.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.favorite.presentation.FavoriteState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteInteractor: FavoriteInteractor,private val context: Context):ViewModel() {

    private val screenState = MutableLiveData<FavoriteState>()
    fun getScreenState(): LiveData<FavoriteState> = screenState
    init {
        fillData()
    }


    fun fillData() {

        viewModelScope.launch {
            favoriteInteractor.getFavoriteTrack()
                .collect{track ->
                    processResult(track)
            }
        }
    }
    private fun processResult(track: List<Track>) {
        if (track.isEmpty()) {
            renderState(FavoriteState.Error(context.getString(R.string.mediaEmpty)))
        } else {
            renderState(FavoriteState.Content(track))
        }
    }


    private fun renderState(state: FavoriteState) {
        screenState.postValue(state)
    }
}








