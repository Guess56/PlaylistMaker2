package com.example.playlistmaker.setting.data.repositories

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.domain.model.EmailData
import com.example.playlistmaker.setting.domain.repositories.SharingRepository

class SharingRepositoryImpl(val context : Context) :SharingRepository {

     override fun getShareAppLink(): String {
        return context.getString(R.string.practicumUrl)
    }

    override fun getSupportEmailData():EmailData {
        return EmailData(
            context.getString(R.string.myEmail),
            context.getString(R.string.themeMessage),
            context.getString(R.string.emailText))
    }

     override fun getTermsLink(): String {
        return context.getString(R.string.agreement)
    }
}