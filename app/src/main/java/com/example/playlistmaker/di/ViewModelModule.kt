package com.example.playlistmaker.di

import com.example.playlistmaker.media.presentation.MediaViewModel
import com.example.playlistmaker.media.presentation.TabFavoriteViewModel
import com.example.playlistmaker.media.presentation.TabMediaViewModel
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackHistoryViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel{
        TrackSearchViewModel(get())
    }
    viewModel {
        MediaPlayerViewModel(get())
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
        TabMediaViewModel()
    }
    viewModel{
        TabFavoriteViewModel()
    }
}

