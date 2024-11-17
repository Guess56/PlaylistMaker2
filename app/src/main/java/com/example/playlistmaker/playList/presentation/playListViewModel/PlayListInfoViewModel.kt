package com.example.playlistmaker.playList.presentation.playListViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.player.presentation.state.PlayListTrackState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class PlayListInfoViewModel(private val dbInteractor: PlayListDbInteractor): ViewModel() {


    private val playListState = MutableLiveData<PlayListIdState>()

    fun getPlayListState(): LiveData<PlayListIdState> = playListState

    private val trackPlaylistState = MutableLiveData<PlayListTrackGetState>()
    fun getTrackPlayListState(): LiveData<PlayListTrackGetState> = trackPlaylistState
    fun getPlayList(id: Int) {
        viewModelScope.launch {
            dbInteractor.getPlayListId(id).collect { playList ->
                processResult(playList)
            }
        }
    }

    private fun processResult(playList: PlayListEntity) {
        if (playList.equals("")) {
            renderState(PlayListIdState.Error("нет данных"))
        } else renderState(PlayListIdState.Content(playList))
    }

    private fun renderState(state: PlayListIdState) {
        playListState.postValue(state)
    }

    fun getTrackPlayList(trackId: String) {
        viewModelScope.launch {
            dbInteractor.getPlayListTrackId().collect { track ->
                result(track)
            }
        }
    }

    private fun result(track: List<PlayListTrackEntity>) {
        if (track.isEmpty()) {
            render(PlayListTrackGetState.Error("нет данных"))
        } else render(PlayListTrackGetState.Content(track))

    }

    private fun render(state: PlayListTrackGetState) {
        trackPlaylistState.postValue(state)
    }

    fun checkTrack(id: List<String>,track:List<PlayListTrackEntity>):List<PlayListTrackEntity>{
    val list = mutableListOf<PlayListTrackEntity>()

        for (i in track)
            for( j in id)
            if (i.trackId.toString() == j)
                list.add(i)
        return list
            }

    fun sumDuration(track:List<PlayListTrackEntity>):Int{
        var duration:Int = 0
        for (i in track){
            duration+=i.trackTimeMillis
        }
        return duration
    }
    }


