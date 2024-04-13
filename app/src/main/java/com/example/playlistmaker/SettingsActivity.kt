package com.example.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar



class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<Toolbar>(R.id.toolbar)

        backButton.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}