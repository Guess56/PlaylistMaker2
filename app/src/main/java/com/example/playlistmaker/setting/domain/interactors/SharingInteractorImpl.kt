package com.example.playlistmaker.setting.domain.interactors

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.ExternalNavigator
import com.example.playlistmaker.setting.data.repositories.SharingRepositoryImpl
import com.example.playlistmaker.setting.domain.model.EmailData
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.repositories.SharingRepository

class SharingInteractorImpl(val repository: SharingRepository,private val externalNavigator: ExternalNavigator):SharingInteractor {
    override fun shareApp() {
        return externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        return externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        return externalNavigator.openEmail(getSupportEmailData())
    }
    private fun getShareAppLink(): String {

    return repository.getShareAppLink()
    }
    private fun getSupportEmailData(): EmailData {
    return repository.getSupportEmailData()
    }

    private fun getTermsLink(): String {
        return repository.getTermsLink()
    }
}