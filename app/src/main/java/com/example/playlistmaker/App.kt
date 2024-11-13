package com.example.playlistmaker

import android.app.Application

import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.markodevcic.peko.PermissionRequester
import kotlinx.coroutines.flow.subscribe
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App:Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
        PermissionRequester.initialize(applicationContext)
        val switchTheme: SwitchThemeInteractor by inject()
        val darkTheme = switchTheme.getSharedPreferencesThemeValue()
        switchTheme.switchTheme(darkTheme)


    }
}
