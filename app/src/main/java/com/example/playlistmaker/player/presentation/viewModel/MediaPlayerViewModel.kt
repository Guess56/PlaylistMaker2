package com.example.playlistmaker.player.presentation.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.favorite.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.domain.interactor.PlayListInteractor
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListState

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayListTrackState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.db.interactor.TrackDbInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale



class MediaPlayerViewModel(interactor: MediaPlayerInteractor, private val favoriteInteractor: FavoriteInteractor,
                           private val interactorDbPlayListDbInteractor: PlayListDbInteractor,private val trackDbInteractor: TrackDbInteractor):ViewModel() {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETE = 4
        const val DELAY = 300L
        const val compliteTrack = "00:00"
    }


    val mediaPlayerInteracror = interactor

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }


    private val mediaPlayerState = MutableLiveData<Int>(STATE_DEFAULT)

    val state: LiveData<Int> get() = mediaPlayerState

    private val _info = MutableLiveData(PlayerState())
    val info: LiveData<PlayerState> get() = _info


    private val _inFavorite = MutableLiveData<Boolean>()
    fun inFavorite(): LiveData<Boolean> = _inFavorite

    private val _addTrack = MutableLiveData<Boolean>()
    fun addTrack(): LiveData<Boolean> = _addTrack

    private val playListState = MutableLiveData<PlayListState>()
    fun getPlayListState(): LiveData<PlayListState> = playListState


    private var timerJob: Job? = null


    private fun resetInfo() {
        mediaPlayerInteracror.releasePlayer()
        stopProgressUpdates()
    }

    private fun stopProgressUpdates() {
        timerJob?.cancel()
    }
    fun getToast(){
        addTrack()
    }
    fun updatePlayList(playList:PlayListEntity,idTrack:String){
        viewModelScope.launch {
            when(mediaPlayerInteracror.addTrackToPlayList(playList,idTrack)){
                false ->{
                    _addTrack.postValue(false)
                    Log.d("Sprint 22","livedata false Трек не добавился т.к. уже есть ")
                }
                true->{
                    _addTrack.postValue(true)
                    viewModelScope.launch {
                       val  track = trackDbInteractor.getTrackIds(idTrack.toLong())
                        Log.d("Sprint 22","livedata true добавляет трек =$track")
                        interactorDbPlayListDbInteractor.insertTrackPlayList(track.toTrackPlayListEntity())
                    }
                }
            }
        }
    }

    fun preparePlayer(url: String) {
        if (mediaPlayerState.value == STATE_DEFAULT)
            mediaPlayerInteracror.prepare(
                url,
                onPrepared = { mediaPlayerState.value = STATE_PREPARED },
                onCompletion = {
                    mediaPlayerState.value = STATE_COMPLETE
                    _info.value = info.value?.copy(currentPosition = compliteTrack)
                    stopProgressUpdates()
                }
            )
    }

    fun formatReleaseDate(releaseDate: String?): String {
        return releaseDate?.substring(0, 4) ?: "Unknown"
    }


    fun formatArtworkUrl(artworkUrl: String?): String? {
        return artworkUrl?.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun pausePlayer() {
        mediaPlayerInteracror.pausePlayer()
        mediaPlayerState.value = STATE_PAUSED
        timerJob?.cancel()

    }

    fun startPlayer() {
        mediaPlayerInteracror.startPlayer()
        mediaPlayerState.value = STATE_PLAYING
        startTimer()

    }

    override fun onCleared() {
        super.onCleared()
        resetInfo()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteracror.isPlaying()) {
                delay(DELAY)
                val formatTime = dateFormat.format(mediaPlayerInteracror.getCurrentPosition())
                _info.value = info.value?.copy(currentPosition = formatTime)
            }
        }
    }

    fun onFavoriteClicked(track:Track) {
        when(track.inFavorite){
            false -> {
                viewModelScope.launch {
                    track.inFavorite = true
                    favoriteInteractor.insertFavoriteTrack(track.toFavoriteEntity())
                    _inFavorite.postValue(true)
                }

            }
                true -> {
                    viewModelScope.launch {
                        favoriteInteractor.deleteFavoriteTrack(track.toFavoriteEntity())
                        track.inFavorite = false
                        _inFavorite.postValue(false)
                    }
                }
            }
    }
    fun getPlayList() {
        viewModelScope.launch {
            interactorDbPlayListDbInteractor.getPlayList().collect{playList ->
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

    fun checkState(track: Track){
        when(track.inFavorite){
            false -> {
                viewModelScope.launch {
                    _inFavorite.postValue(false)
                }

            }
            true -> {
                viewModelScope.launch {
                    _inFavorite.postValue(true)
                }
            }
        }
    }
}
    fun Track.toFavoriteEntity() = FavoriteEntity(
        trackId.toLong(),
        trackName,
        country,
        releaseDate,
        collectionName,
        primaryGenreName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        previewUrl,
        inFavorite,
        System.currentTimeMillis()
    )
fun TrackEntity.toTrackPlayListEntity() = PlayListTrackEntity(
    trackId.toLong(),
    trackName,
    country,
    releaseDate,
    collectionName,
    primaryGenreName,
    artistName,
    trackTimeMillis,
    artworkUrl100,
    previewUrl,
    inFavorite,
    System.currentTimeMillis()
)

fun createJsonFromTracks(tracks: ArrayList<String>): String {
    return Gson().toJson(tracks)
}

fun createTracksFromJson(json: String): ArrayList<String> {
    if (json == "") return ArrayList()
    val trackListType = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(json, trackListType)
}















