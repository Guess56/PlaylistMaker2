package com.example.playlistmaker

import java.sql.Date

data class Track (
    val trackId:String,
    val trackName:String,
    val country:String,
    val releaseDate:java.util.Date,
    val collectionName:String,
    val primaryGenreName:String,
    val artistName:String,
    val trackTimeMillis:Int,
    val artworkUrl100:String
)

