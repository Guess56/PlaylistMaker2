package com.example.playlistmaker.setting

import com.example.playlistmaker.setting.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(text:String)
    fun openLink(link:String)
    fun openEmail(email:EmailData)
}