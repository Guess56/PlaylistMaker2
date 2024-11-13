package com.example.playlistmaker.playList.presentation.playListViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.R
import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.favorite.presentation.FavoriteState
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

class CreatePlayListViewModel( val interactor: com.example.playlistmaker.playList.domain.interactor.PlayListInteractor,val appDataBase: AppDataBase,private val dbInteractor: PlayListDbInteractor):ViewModel() {


    private var namePlaylist: String = ""
    private var descriptionPlayList: String = ""
    private var filePath: String = ""
    private val playList = ArrayList<PlayList>()
    private val playListEntity = ArrayList<PlayListEntity>()

    private val bottomState = MutableLiveData<Boolean>()
    fun getBottomState(): LiveData<Boolean> = bottomState

    private val playListState = MutableLiveData<PlayListState>()
    fun getPlayListState(): LiveData<PlayListState> = playListState
    fun checkBottom(textName: String) {
        if (textName.isNotBlank()) {
            viewModelScope.launch {
                bottomState.postValue(true)
            }
        } else {
            viewModelScope.launch {
                bottomState.postValue(false)
            }
        }
    }

    fun saveName(name: String) {
        this.namePlaylist = name
    }

    fun saveDescription(description: String) {
        this.descriptionPlayList = description
    }

    fun saveFilePath(file: String) {
        this.filePath = file
    }

    fun getPlayList() {
        viewModelScope.launch {
            dbInteractor.getPlayList().collect{playList ->
                processResult(playList)

            }
        }
    }

    private fun processResult(playList: List<PlayListEntity>) {
        if (playList.isEmpty()) {
            renderState(PlayListState.Error("Ваша медиатека пуста"))
        } else {
            renderState(PlayListState.Content(playList))
        }
    }


    private fun renderState(state: PlayListState) {
        playListState.postValue(state)
    }



    fun savePlayList() {
        val list = interactor.savePlayList(namePlaylist, descriptionPlayList, filePath)
        this.playList.add(list)
        playList.size


        this.playListEntity.add(list.toPlayListEntity())
        viewModelScope.launch { appDataBase.playListDao().insertPlayList(playListEntity) }

    }

    fun PlayList.toPlayListEntity() = PlayListEntity(
        playListId,
        namePlayList,
        image,
        description,
        filePath,
        count,
        trackId.toString(),
    )
}



