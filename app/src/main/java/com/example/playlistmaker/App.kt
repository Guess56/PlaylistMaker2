package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App:Application() {
    var darkTheme = false
        private set
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(NAME_THEME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(KEY_THEME,false)
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled : Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else  {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object {
        private const val NAME_THEME = "name_theme"
        private const val KEY_THEME= "key_theme"
    }
}
