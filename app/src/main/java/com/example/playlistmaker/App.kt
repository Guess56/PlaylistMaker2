package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.Creator.Creator


class App:Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val sharedPreferencesInteractor = Creator.provideSwitchThemeInteractor()
        val darkTheme = Creator.provideSwitchThemeRepository().getSharedPreferencesThemeValue()
        sharedPreferencesInteractor.switchTheme(darkTheme)
    }
}
