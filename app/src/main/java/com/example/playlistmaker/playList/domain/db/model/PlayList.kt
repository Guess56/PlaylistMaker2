package com.example.playlistmaker.playList.domain.db.model

data class PlayList (
    val playListId:Int,
    val namePlayList:String,
    val image:String,
    val description:String,
    val filePath:String,
    val count:Int,
    val trackId:String,
)
