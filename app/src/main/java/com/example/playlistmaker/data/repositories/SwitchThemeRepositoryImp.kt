package com.example.playlistmaker.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.repositories.SwitchThemeRepository

class SwitchThemeRepositoryImp():SwitchThemeRepository {
    companion object {
        private const val NAME_THEME = "name_theme"
        private const val KEY_THEME= "key_theme"
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else  {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun sharedPreferencesEdit(checked : Boolean) {
        val sharedPref = Creator.provideSharedPreferences(NAME_THEME)
        sharedPref.edit()
            .putBoolean(KEY_THEME,checked)
            .apply()
    }

    override fun getSharedPreferencesThemeValue():Boolean{
        val sharedPref = Creator.provideSharedPreferences(NAME_THEME)
        val darkTheme = sharedPref.getBoolean(KEY_THEME,false)
        return darkTheme
    }
}