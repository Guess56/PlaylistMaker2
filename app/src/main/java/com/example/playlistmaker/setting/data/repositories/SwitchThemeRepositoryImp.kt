package com.example.playlistmaker.setting.data.repositories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository

class SwitchThemeRepositoryImp(private val sharedPref:SharedPreferences): SwitchThemeRepository {
    companion object {

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

        sharedPref.edit()
            .putBoolean(KEY_THEME,checked)
            .apply()
    }

    override fun getSharedPreferencesThemeValue():Boolean{

        val darkTheme = sharedPref.getBoolean(KEY_THEME,false)
        return darkTheme
    }
}