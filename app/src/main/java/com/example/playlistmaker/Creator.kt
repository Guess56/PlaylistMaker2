package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.HistoryInteraktorImp
import com.example.playlistmaker.domain.interactors.TrackInteractor
import com.example.playlistmaker.domain.repositories.TrackRepository
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.impl.SharedPreferencesInteractorImp
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.interactors.HistoryInteractor
import com.example.playlistmaker.domain.interactors.SharedPreferencesInteractor
import com.example.playlistmaker.domain.repositories.SearchHistoryRepository

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

    fun provideSearchHistoryRepository(key : String): SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(provideSharedPreferences(key))
    }

    fun provideSharedPreferences(key:String):SharedPreferences{
    val keyPref = key
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }

    fun getHistoryRepository() : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSharedPreferences("histori_name"))
    }
    fun provideHistoryInteractor():HistoryInteractor {
        return HistoryInteraktorImp(getHistoryRepository())
    }

    fun providesharedpreferencesinteractor() : SharedPreferencesInteractor {
        return SharedPreferencesInteractorImp()
    }

}
