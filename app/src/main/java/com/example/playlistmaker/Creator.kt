package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.interactors.HistoryInteractorImp
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.repositories.TrackRepository
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repositories.SwitchThemeRepositoryImp
import com.example.playlistmaker.domain.interactors.SwitchThemeInteractorImp
import com.example.playlistmaker.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.repositories.SwitchThemeRepository

object Creator {
    private lateinit var application: Application
    const val HISTORY_NAME = "histori_name"



    fun initApplication(application: Application){
        this.application = application
    }
    private  fun provideTrackRepository (): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(provideTrackRepository())
    }

    fun provideSharedPreferences(key:String):SharedPreferences{
    val keyPref = key
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }

    fun provideHistoryRepository() : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSharedPreferences(HISTORY_NAME))
    }
    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImp(provideHistoryRepository())
    }

    fun provideSwitchThemeInteractor() : SwitchThemeInteractor {
        return SwitchThemeInteractorImp(provideSwitchThemeRepository())
    }

    fun provideSwitchThemeRepository() : SwitchThemeRepository {
        return SwitchThemeRepositoryImp()
    }


}

