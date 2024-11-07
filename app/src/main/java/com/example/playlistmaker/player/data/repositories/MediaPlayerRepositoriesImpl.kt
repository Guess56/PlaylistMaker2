package com.example.playlistmaker.player.data.repositories

import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.player.presentation.viewModel.createJsonFromTracks
import com.example.playlistmaker.player.presentation.viewModel.createTracksFromJson

class MediaPlayerRepositoriesImpl(private val appDataBase: AppDataBase): MediaPlayerRepositories {

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

    override suspend fun addTrackToPlayList(playList: PlayListEntity, idTrack: String): Boolean {

        var list = ArrayList<String>()
        if (playList.trackId.isEmpty()) {
            list.add(idTrack)
            appDataBase.playListDao().updatePlayList(
                playList.copy(
                    trackId = createJsonFromTracks(list),
                    count = list.size
                )
            )
            return true
        }
        val json = playList.trackId

        if (json.isNotEmpty()) {
            list = createTracksFromJson(json)
            val current = list.filter { track -> track == idTrack }
            if (current.isEmpty()) {
                list.add(idTrack)
                appDataBase.playListDao().updatePlayList(
                    playList.copy(
                        trackId = createJsonFromTracks(list),
                        count = list.size
                    )
                )
                return true
            }

        }
        return false
    }
}



