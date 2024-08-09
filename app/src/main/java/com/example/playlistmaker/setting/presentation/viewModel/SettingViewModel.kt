package com.example.playlistmaker.setting.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

class SettingViewModel: ViewModel() {


    val sharingInteractor = Creator.provideSharingInteractor()
    val switchThemeInteractor = Creator.provideSwitchThemeInteractor()
   fun getTheme():Boolean{
      return switchThemeInteractor.getSharedPreferencesThemeValue()
   }
    fun editTheme(checked:Boolean){
        switchThemeInteractor.sharedPreferencesEdit(checked)
    }
    fun switchTheme(checked: Boolean){
        switchThemeInteractor.switchTheme(checked)
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
                SettingViewModel()
            }
        }
    }

}