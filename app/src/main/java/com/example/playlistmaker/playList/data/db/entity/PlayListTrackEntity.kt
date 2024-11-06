package com.example.playlistmaker.playList.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_play_list")
data class PlayListTrackEntity(
    @PrimaryKey
    val trackId:Long,
    val trackName:String,
    val country:String,
    val releaseDate:String,
    val collectionName:String,
    val primaryGenreName:String,
    val artistName:String,
    val trackTimeMillis:Int,
    val artworkUrl100:String,
    val previewUrl : String,
    val inFavorite : Boolean,
    val timeAdd: Long
)

