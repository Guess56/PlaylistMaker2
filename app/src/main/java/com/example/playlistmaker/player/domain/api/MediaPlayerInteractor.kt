package com.example.playlistmaker.player.domain.api

interface MediaPlayerInteractor {
    fun prepare(trackUrl:String,onPrepared:()->Unit,onCompletion:()->Unit)
    fun startPlayer()
    fun pausePlayer()
    fun resetPlayer()
    fun releasePlayer()
    fun  isPlaying(): Boolean
    fun getCurrentPosition(): Int
}