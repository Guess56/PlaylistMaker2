package com.example.playlistmaker.search.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId:String,
    val trackName:String,
    val country:String,
    val releaseDate:String,
    val collectionName:String,
    val primaryGenreName:String,
    val artistName:String,
    val trackTimeMillis:Int,
    val artworkUrl100:String,
    val previewUrl : String,
    var inFavorite : Boolean,
    var addTrack:Long
)
