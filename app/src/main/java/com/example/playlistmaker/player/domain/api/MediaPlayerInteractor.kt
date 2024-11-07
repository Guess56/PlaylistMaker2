package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

interface MediaPlayerInteractor {
    fun prepare(trackUrl:String,onPrepared:()->Unit,onCompletion:()->Unit)
    fun startPlayer()
    fun pausePlayer()
    fun resetPlayer()
    fun releasePlayer()
    fun  isPlaying(): Boolean
    fun getCurrentPosition(): Int
    suspend fun addTrackToPlayList(playList: PlayListEntity, idTrack:String):Boolean
}