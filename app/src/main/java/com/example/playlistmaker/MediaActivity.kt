package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class MediaActivity : AppCompatActivity() {
    companion object {
        private const val KEY = "key"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT
    private lateinit var playOrPauseButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val backButton = findViewById<Toolbar>(R.id.toolbarBack)


        val intent = getIntent()
        val track: String? = intent.getStringExtra(KEY)
        val gson = Gson()
        val historyTrackClick = gson.fromJson(track, Track::class.java)

        val tv_trackName = findViewById<TextView>(R.id.name)
        val tv_albumName = findViewById<TextView>(R.id.trackName)
        val tv_duration = findViewById<TextView>(R.id.duration)
        val tv_country = findViewById<TextView>(R.id.country)
        val tv_years = findViewById<TextView>(R.id.year)
        val tv_album = findViewById<TextView>(R.id.album)
        val tv_genre = findViewById<TextView>(R.id.genre)
        val iv_title = findViewById<ImageView>(R.id.title)
        tv_trackName.text = historyTrackClick.trackName
        tv_albumName.text = historyTrackClick.artistName
        tv_country.text = historyTrackClick.country
        tv_genre.text = historyTrackClick.primaryGenreName
        tv_album.text = historyTrackClick.collectionName
        tv_years.text =
            SimpleDateFormat("YYYY", Locale.getDefault()).format(historyTrackClick.releaseDate)
        tv_duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(historyTrackClick.trackTimeMillis)
        val url = historyTrackClick.previewUrl
        val playOrPauseButton = findViewById<ImageView>(R.id.playOrPause)


        Glide.with(this)
            .load(historyTrackClick.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.artWorkUrl100_radius_media)))
            .into(iv_title)

        backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }
}



