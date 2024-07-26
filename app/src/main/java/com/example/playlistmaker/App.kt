package com.example.playlistmaker

import android.app.Application


class App:Application() {
    var darkTheme = false
        private set
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val sharedPreferencesInteractor = Creator.provideSwitchThemeInteractor()
        val sharedPreferences = Creator.provideSharedPreferences(NAME_THEME)
        darkTheme = sharedPreferences.getBoolean(KEY_THEME,false)
        sharedPreferencesInteractor.switchTheme(darkTheme)

    }

    companion object {
        private const val NAME_THEME = "name_theme"
        private const val KEY_THEME= "key_theme"
    }
}
