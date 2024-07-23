package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

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

    fun provideSearchHistoryRepository(key : String):SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(provideSharedPreferences(key))
    }
    fun provideSharedPreferences(key:String):SharedPreferences{
    val keyPref = key
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }
}
