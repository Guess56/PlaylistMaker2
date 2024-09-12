package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlistmaker.menu.ui.MenuFragment

class MainActivity:AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            supportFragmentManager.commit {
                add(R.id.fragment_container,MenuFragment())
            }
        }
    }

}