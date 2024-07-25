package com.example.playlistmaker.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.interactors.SharedPreferencesInteractor

class SharedPreferencesInteractorImp:SharedPreferencesInteractor {
    private var darkTheme = false
    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else  {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}