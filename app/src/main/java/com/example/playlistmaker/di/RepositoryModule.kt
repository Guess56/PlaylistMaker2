package com.example.playlistmaker.di

import com.example.playlistmaker.favorite.data.db.converter.FavoriteTrackDbConverter
import com.example.playlistmaker.player.data.repositories.MediaPlayerRepositoriesImpl
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import com.example.playlistmaker.search.data.converters.TrackDbConverter
import com.example.playlistmaker.favorite.data.db.repository.FavoriteRepositoryImpl
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.favorite.domain.repository.FavoriteRepository
import com.example.playlistmaker.playList.data.db.repository.PlayListDbRepositoryImpl
import com.example.playlistmaker.playList.data.repository.PlayListRepositoryImpl
import com.example.playlistmaker.playList.domain.db.repository.PlayListDbRepository
import com.example.playlistmaker.playList.domain.repository.PlayListRepository
import com.example.playlistmaker.search.data.db.repository.TrackDbRepositoryImpl
import com.example.playlistmaker.search.domain.db.repository.TrackDbRepository
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.setting.data.repositories.ExternalNavigatorRepositorympl
import com.example.playlistmaker.setting.data.repositories.SharingRepositoryImpl
import com.example.playlistmaker.setting.data.repositories.SwitchThemeRepositoryImp
import com.example.playlistmaker.setting.domain.repositories.ExternalNavigatorRepository
import com.example.playlistmaker.setting.domain.repositories.SharingRepository
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(),get(),get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(),get())
    }
    single<SwitchThemeRepository> {
        SwitchThemeRepositoryImp(get())

    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositorympl(get())
    }

    factory<MediaPlayerRepositories> {
        MediaPlayerRepositoriesImpl()
    }
    factory { TrackDbConverter() }

    factory { FavoriteTrackDbConverter() }

    factory<TrackDbRepository> {
        TrackDbRepositoryImpl(get(),get())
    }

    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(get(),get())
    }

    factory<PlayListDbRepository> {
        PlayListDbRepositoryImpl(get())
    }
    factory<PlayListRepository> {
        PlayListRepositoryImpl()
    }



}