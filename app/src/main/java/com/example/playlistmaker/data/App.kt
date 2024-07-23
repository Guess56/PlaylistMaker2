package com.example.playlistmaker.data

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator


class App:Application() {
    var darkTheme = false
        private set
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val sharedPreferences = Creator.provideSharedPreferences(NAME_THEME)
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
