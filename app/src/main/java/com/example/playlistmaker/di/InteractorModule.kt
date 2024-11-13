package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.interactors.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.favorite.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.favorite.domain.interactorimpl.FavoriteInteractorImpl
import com.example.playlistmaker.playList.domain.db.interactor.PlayListDbInteractor
import com.example.playlistmaker.playList.domain.db.interactorImpl.PlaylistDbInteractorImpl
import com.example.playlistmaker.playList.domain.interactor.PlayListInteractor
import com.example.playlistmaker.playList.domain.interactorImp.PlayListInteractorImpl
import com.example.playlistmaker.search.domain.db.interactor.TrackDbInteractor
import com.example.playlistmaker.search.domain.db.interactorImpl.TrackDbInteractorImpl
import com.example.playlistmaker.search.domain.interactors.HistoryInteractorImp
import com.example.playlistmaker.search.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.setting.domain.interactors.SharingInteractorImpl
import com.example.playlistmaker.setting.domain.interactors.SwitchThemeInteractorImp
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImp(get())
    }

    single<SwitchThemeInteractor> {
        SwitchThemeInteractorImp(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(),get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<TrackDbInteractor> {
        TrackDbInteractorImpl(get())
    }
    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    factory<PlayListDbInteractor> {
        PlaylistDbInteractorImpl(get())
    }
    factory<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }

}