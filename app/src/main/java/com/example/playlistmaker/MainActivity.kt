package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttomSearch = findViewById<Button>(R.id.search)
        buttomSearch.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск", Toast.LENGTH_SHORT).show()
        }
        val buttomMedia = findViewById<Button>(R.id.media)
        val buttomClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека", Toast.LENGTH_SHORT).show()
            }
        }
        buttomMedia.setOnClickListener(buttomClickListener)
        val buttomOption = findViewById<Button>(R.id.option)
        buttomOption.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}