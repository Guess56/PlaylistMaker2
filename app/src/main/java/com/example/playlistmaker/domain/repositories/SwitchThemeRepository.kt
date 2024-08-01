package com.example.playlistmaker.domain.repositories

interface SwitchThemeRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun sharedPreferencesEdit(checked :Boolean)
    fun getSharedPreferencesThemeValue():Boolean
}