package com.example.playlistmaker

import android.app.Application


class App:Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val sharedPreferencesInteractor = Creator.provideSwitchThemeInteractor()
        val darkTheme = Creator.getSwitchThemeRepository().getSharedPreferencesThemeValue()
        sharedPreferencesInteractor.switchTheme(darkTheme)
    }
}
