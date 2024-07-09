package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val optionButton = findViewById<Button>(R.id.option)
        optionButton.setOnClickListener{
            val optionIntetnt = Intent(this,SettingsActivity::class.java)
            startActivity(optionIntetnt)
        }
        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener{
            val searchIntetnt = Intent(this, SearchActivity::class.java)
            startActivity(searchIntetnt)
        }
        val mediaButton = findViewById<Button>(R.id.media)
        mediaButton.setOnClickListener{
            val mediaIntetnt = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntetnt)
        }


    }
}
