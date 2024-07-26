package com.example.playlistmaker.domain.interactors

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.repositories.SwitchThemeRepository


class SwitchThemeInteractorImp(private val repository : SwitchThemeRepository): SwitchThemeInteractor {


    override fun switchTheme(darkThemeEnabled: Boolean) {
     return repository.switchTheme(darkThemeEnabled)
    }

    override fun sharedPreferencesEdit(checked: Boolean) {
        return repository.sharedPreferencesEdit(checked)
    }

    override fun getSharedPreferencesThemeValue():Boolean {
        return repository.getSharedPreferencesThemeValue()
    }

}