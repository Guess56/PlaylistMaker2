package com.example.playlistmaker.setting.domain.repositories

import com.example.playlistmaker.setting.domain.model.EmailData

interface ExternalNavigatorRepository {
    fun shareLink(text:String)
    fun openLink(link:String)
    fun openEmail(email:EmailData)
}