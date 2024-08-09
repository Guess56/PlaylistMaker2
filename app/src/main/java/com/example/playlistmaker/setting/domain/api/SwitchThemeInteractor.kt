package com.example.playlistmaker.setting.domain.api

interface SwitchThemeInteractor {
    fun switchTheme(darkThemeEnabled : Boolean)
    fun sharedPreferencesEdit(checked : Boolean)
    fun getSharedPreferencesThemeValue():Boolean
}