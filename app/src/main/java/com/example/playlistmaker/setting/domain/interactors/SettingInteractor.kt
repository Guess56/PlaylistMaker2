package com.example.playlistmaker.setting.domain.interactors

import com.example.playlistmaker.setting.domain.model.ThemeSetting

interface SettingInteractor {
    fun getThemeSettings(): ThemeSetting
    fun updateThemeSetting(settings: ThemeSetting)
}