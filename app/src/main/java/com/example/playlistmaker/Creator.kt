package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import kotlin.properties.Delegates

object Creator {
    private lateinit var application: Application


    fun initApplication(application: Application){
        this.application = application
    }
    private  fun getTrackRepository (): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
    fun provideSharedPreferences(name:String):SharedPreferences{
       val key = name
    return application.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

}
