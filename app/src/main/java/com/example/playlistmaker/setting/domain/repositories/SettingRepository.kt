package com.example.playlistmaker.setting.domain.repositories

import com.example.playlistmaker.setting.domain.model.ThemeSetting

interface SettingRepository {
    fun getThemeSettings(): ThemeSetting
    fun updateThemeSetting(settings: ThemeSetting)
}