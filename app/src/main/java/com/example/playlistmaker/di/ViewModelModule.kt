package com.example.playlistmaker.di

import com.example.playlistmaker.media.presentation.viewModel.MediaViewModel
import com.example.playlistmaker.media.presentation.viewModel.TabFavoriteViewModel
import com.example.playlistmaker.favorite.presentation.viewModel.FavoriteViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.CreatePlayListViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListFragmentViewModel
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackHistoryViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel{
        TrackSearchViewModel(get(),get())
    }
    viewModel {
        MediaPlayerViewModel(get(),get(),get())
    }
    viewModel {
        SettingViewModel(get(),get())
    }
    viewModel {
        TrackHistoryViewModel(get())
    }
    viewModel{
        MediaViewModel()
    }
    viewModel{
        PlayListFragmentViewModel()
    }
    viewModel{
        TabFavoriteViewModel()
    }
    viewModel {
        FavoriteViewModel(get(),androidContext())
    }
    viewModel {
        CreatePlayListViewModel(get(),get(),get())
    }
}

