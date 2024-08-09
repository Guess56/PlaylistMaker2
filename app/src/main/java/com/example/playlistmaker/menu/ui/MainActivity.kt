package com.example.playlistmaker.menu.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.media.presentation.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.presentation.SettingsActivity
import com.example.playlistmaker.search.presentation.SearchActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val optionButton = findViewById<Button>(R.id.option)
        optionButton.setOnClickListener{
            val optionIntent = Intent(this, SettingsActivity::class.java)
            startActivity(optionIntent)
        }
        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        val mediaButton = findViewById<Button>(R.id.media)
        mediaButton.setOnClickListener{
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }


    }
}
