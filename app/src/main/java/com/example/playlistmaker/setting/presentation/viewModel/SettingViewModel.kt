package com.example.playlistmaker.setting.presentation.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.Search.presentation.state.TrackSearchState
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.setting.domain.model.ThemeSetting

class SettingViewModel(context: Context): ViewModel() {


    val sharingInteractor = Creator.provideSharingInteractor()
    val switchThemeInteractor = Creator.provideSwitchThemeInteractor()
   fun getTheme():Boolean{
      return switchThemeInteractor.getSharedPreferencesThemeValue()
   }
    fun editTheme(cheked:Boolean){
        switchThemeInteractor.sharedPreferencesEdit(cheked)
    }
    fun switchTheme(cheked: Boolean){
        switchThemeInteractor.switchTheme(cheked)
    }
    fun supportSend(){
        sharingInteractor.openSupport()
    }
    fun openTerm(){
        sharingInteractor.openTerms()
    }
    fun shareApp(){
        sharingInteractor.shareApp()
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

}