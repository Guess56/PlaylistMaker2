package com.example.playlistmaker.player.presentation

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.Search.presentation.state.TrackSearchState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.player.ui.MediaPlayer
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerViewModel(context: Context):ViewModel() {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETE = 4
        const val DELAY = 300L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaPlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }


    val mediaPlayerInteracror = Creator.provideMediaPlayerInteractor()
    val mainThreadHandler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }


    private val mediaPlayerState = MutableLiveData<Int>(STATE_DEFAULT)
    val state: LiveData<Int> get() = mediaPlayerState



    private val _info = MutableLiveData(PlayerState())
    val info : LiveData<PlayerState> get() = _info


    private fun resetInfo() {
        mediaPlayerInteracror.releasePlayer()
        stopProgressUpdates()
    }
    private fun stopProgressUpdates() {
        updateTime().let { mainThreadHandler.removeCallbacks(it) }
    }

     fun preparePlayer(url:String) {
        if (mediaPlayerState.value == STATE_DEFAULT)
            mediaPlayerInteracror.prepare(
                url,
                onPrepared = {mediaPlayerState.value = STATE_PREPARED },
                onCompletion = {mediaPlayerState.value = STATE_COMPLETE
                    resetInfo()
                    stopProgressUpdates() }
            )
        }

    private fun updateTime (): Runnable {

        return object : Runnable {
            override fun run() {
                if (mediaPlayerInteracror.isPlaying()) {
                    val formatTime = dateFormat.format(mediaPlayerInteracror.getCurrentPosition())
                    _info.value = info.value?.copy(currentPosition = formatTime)
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }.also { mainThreadHandler.post(it) }


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
        stopProgressUpdates()
    }
    fun startPlayer() {
        mediaPlayerInteracror.startPlayer()
        mediaPlayerState.value = STATE_PLAYING
        mainThreadHandler.post(updateTime())
    }


        }








