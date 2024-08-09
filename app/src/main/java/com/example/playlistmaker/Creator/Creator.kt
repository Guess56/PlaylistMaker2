package com.example.playlistmaker.Creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.Search.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.Search.domain.interactors.HistoryInteractorImp
import com.example.playlistmaker.Search.domain.api.TrackInteractor
import com.example.playlistmaker.Search.domain.repositories.TrackRepository
import com.example.playlistmaker.Search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.setting.data.repositories.SwitchThemeRepositoryImp
import com.example.playlistmaker.setting.domain.interactors.SwitchThemeInteractorImp
import com.example.playlistmaker.Search.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.Search.domain.api.HistoryInteractor
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.Search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.player.data.interactors.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.repositories.MediaPlayerRepositoriesImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import com.example.playlistmaker.setting.ExternalNavigator
import com.example.playlistmaker.setting.data.repositories.SharingRepositoryImpl
import com.example.playlistmaker.setting.domain.ExternalNavigatorImpl
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.interactors.SharingInteractorImpl
import com.example.playlistmaker.setting.domain.repositories.SharingRepository
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository

object Creator {
    private lateinit var application: Application
    const val HISTORY_NAME = "histori_name"



    fun initApplication(application: Application){
        Creator.application = application
    }

    private  fun provideTrackRepository (context: Context): TrackRepository {
        return TrackRepositoryImpl(
            com.example.playlistmaker.Search.data.network.RetrofitNetworkClient(
                context
            )
        )
    }
    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(provideTrackRepository(context))
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


    fun provideSharingInteractor() : SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(), provideExternalNavigator())
    }
    fun provideSharingRepository():SharingRepository {
        return SharingRepositoryImpl(application)
    }
    fun provideExternalNavigator(): ExternalNavigator{
        return ExternalNavigatorImpl(application)
    }
    fun provideMediaPlayerInteractor():MediaPlayerInteractor{
        return  MediaPlayerInteractorImpl(MediaPlayerRepositories())
    }
    fun MediaPlayerRepositories():MediaPlayerRepositories{
        return MediaPlayerRepositoriesImpl()
    }


}

