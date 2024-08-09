package com.example.playlistmaker.player.domain.repositories

import com.example.playlistmaker.Search.domain.models.Track

interface MediaPlayerRepositories {
fun prepare(trackUrl:String,onPrepared:()->Unit,onCompletion:()->Unit)
fun startPlayer()
fun pausePlayer()
fun resetPlayer()
fun releasePlayer()
fun  isPlaying(): Boolean
fun getCurrentPosition(): Int
}