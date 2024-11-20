package com.example.playlistmaker.playList.presentation.playListViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.interactor.PlayListInteractor
import com.example.playlistmaker.player.presentation.state.PlayListTrackState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayListInfoViewModel( val dbInteractor: PlayListDbInteractor, private val interactor : PlayListInteractor): ViewModel() {


    private val playListState = MutableLiveData<PlayListIdState>()
    lateinit var playListSet:PlayListEntity


    fun getPlayListState(): LiveData<PlayListIdState> = playListState

    private val trackPlaylistState = MutableLiveData<PlayListTrackGetState>()
    fun getTrackPlayListState(): LiveData<PlayListTrackGetState> = trackPlaylistState

    private val listPlayList = MutableLiveData<ListPlayListState>()
   fun get(): LiveData<ListPlayListState> = listPlayList

    fun getListPlayList(){
        viewModelScope.launch {
            dbInteractor.getPlayList().collect{playlist ->
              resultList(playlist)
            }
        }
    }
    private fun resultList(playList: List<PlayListEntity>) {
        if (playList.isEmpty()) {
            renderList(ListPlayListState.Error("нет данных"))
        } else renderList(ListPlayListState.Content(playList))
    }
    private fun renderList(state: ListPlayListState) {
        listPlayList.postValue(state)
    }



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

    fun getTrackPlayList() {
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

    fun setPlayList(playList: PlayListEntity){
      playListSet = playList
    }

    fun sumDuration(track:List<PlayListTrackEntity>):Int{
        var duration:Int = 0
        for (i in track){
            duration+=i.trackTimeMillis
        }
        return duration
    }
    suspend fun calcPlaylistsWithTrackCount(trackId: String): Int {
        val playlists = dbInteractor.getPlayList().first()
        return playlists.count { it.trackId.contains(trackId) }
    }

    @Synchronized
    fun delete(deleteId:String,playlist:List<String>){
        viewModelScope.launch {
            val needDeleteTrack = calcPlaylistsWithTrackCount(deleteId) <= 1
        if (needDeleteTrack){
                val track = dbInteractor.getTrack(deleteId.toLong())
                dbInteractor.deleteTrackDb(track)
                interactor.deletePlayListTrack(playlist, deleteId, playListSet)
            } else {
                interactor.deletePlayListTrack(playlist,deleteId,playListSet)
            }
        }
    }
}


