package com.example.playlistmaker.setting.ui


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModelSetting: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        viewModelSetting = ViewModelProvider(
            this,
            SettingViewModel.getViewModelFactory()
        )[SettingViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<Toolbar>(R.id.toolbar)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switcherTheme)


        switchTheme.isChecked = viewModelSetting.getTheme()
        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModelSetting.editTheme(checked)
            viewModelSetting.switchTheme(checked)

        }

        backButton.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }



        val supportButton = findViewById<TextView>(R.id.textVievSupport)
        supportButton.setOnClickListener{
            viewModelSetting.supportSend()
        }
        val agreementButton = findViewById<TextView>(R.id.textVievAgreement)
        agreementButton.setOnClickListener{
            viewModelSetting.openTerm()
        }
        val shareButton = findViewById<TextView>(R.id.textVievShare)
        shareButton.setOnClickListener{
            viewModelSetting.shareApp()
        }
    }
}
