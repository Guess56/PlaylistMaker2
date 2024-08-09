package com.example.playlistmaker.player.data.interactors

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories

class MediaPlayerInteractorImpl(private val repository:MediaPlayerRepositories):MediaPlayerInteractor {
    override fun prepare(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        return repository.prepare(trackUrl, onPrepared, onCompletion)
    }

    override fun startPlayer() {
        return repository.startPlayer()
    }

    override fun pausePlayer() {
        return repository.pausePlayer()
    }

    override fun resetPlayer() {
        return repository.resetPlayer()
    }

    override fun releasePlayer() {
        return repository.releasePlayer()
    }

    override fun isPlaying(): Boolean {
        return  repository.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }
}