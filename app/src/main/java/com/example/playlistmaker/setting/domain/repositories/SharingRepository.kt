package com.example.playlistmaker.setting.domain.repositories

import com.example.playlistmaker.setting.domain.model.EmailData

interface SharingRepository {
    fun getShareAppLink():String
    fun getSupportEmailData(): EmailData
    fun getTermsLink():String
}