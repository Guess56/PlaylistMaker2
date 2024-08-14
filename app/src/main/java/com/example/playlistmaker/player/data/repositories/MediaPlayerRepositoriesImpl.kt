package com.example.playlistmaker.player.data.repositories

import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import android.media.MediaPlayer

class MediaPlayerRepositoriesImpl(): MediaPlayerRepositories {

    private val mediaPlayer = MediaPlayer()
    override fun prepare(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun resetPlayer() {
        mediaPlayer.reset()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }


}

