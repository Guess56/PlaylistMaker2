package com.example.playlistmaker.ui.setting


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<Toolbar>(R.id.toolbar)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switcherTheme)
        switchTheme.isChecked = (applicationContext as App).darkTheme

        val switchThemeInteractor = Creator.provideSwitchThemeInteractor()
        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            switchThemeInteractor.sharedPreferencesEdit(checked)
            switchThemeInteractor.switchTheme(checked)

        }

        backButton.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        val supportButton = findViewById<TextView>(R.id.textVievSupport)
        supportButton.setOnClickListener{
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf( getString(R.string.myEmail)))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.themeMessage))
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.emailText))
            startActivity(mailIntent)
        }
        val agreementButton = findViewById<TextView>(R.id.textVievAgreement)
        agreementButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(getString(R.string.agreement))
            startActivity(browserIntent);
        }
        val shareButton = findViewById<TextView>(R.id.textVievShare)
        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicumUrl))
            shareIntent.type="text/plain"
            startActivity(shareIntent)
        }
    }
    companion object {
        private const val NAME_THEME = "name_theme"
        private const val KEY_THEME= "key_theme"
    }
}
