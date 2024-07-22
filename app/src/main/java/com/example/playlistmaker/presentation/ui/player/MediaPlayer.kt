package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayer : AppCompatActivity() {
    private companion object {
         const val KEY = "key"
         const val STATE_DEFAULT = 0
         const val STATE_PREPARED = 1
         const val STATE_PLAYING = 2
         const val STATE_PAUSED = 3
         const val DELAY = 300L
    }

    private val mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT
    private lateinit var playOrPauseButton: ImageView
    private var mainThreadHandler: Handler? = null
    private var time : TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val backButton = findViewById<Toolbar>(R.id.toolbarBack)


        val intent = getIntent()
        val track: String? = intent.getStringExtra(KEY)
        val gson = Gson()
        val historyTrackClick = gson.fromJson(track, Track::class.java)

        val tvTrackName = findViewById<TextView>(R.id.name)
        val tvAlbumName = findViewById<TextView>(R.id.trackName)
        val tvDuration = findViewById<TextView>(R.id.duration)
        val tvCountry = findViewById<TextView>(R.id.country)
        val tvYears = findViewById<TextView>(R.id.year)
        val tvAlbum = findViewById<TextView>(R.id.album)
        val tvGenre = findViewById<TextView>(R.id.genre)
        val ivTitle = findViewById<ImageView>(R.id.title)
        tvTrackName.text = historyTrackClick.trackName
        tvAlbumName.text = historyTrackClick.artistName
        tvCountry.text = historyTrackClick.country
        tvGenre.text = historyTrackClick.primaryGenreName
        tvAlbum.text = historyTrackClick.collectionName
        tvYears.text =
            SimpleDateFormat("YYYY", Locale.getDefault()).format(historyTrackClick.releaseDate)
        tvDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(historyTrackClick.trackTimeMillis)
        val url = historyTrackClick.previewUrl

        time = findViewById<TextView>(R.id.time)
        mainThreadHandler = Handler(Looper.getMainLooper())


         playOrPauseButton = findViewById<ImageView>(R.id.playOrPause)

        preparePlayer(url)

        playOrPauseButton.setOnClickListener {
            playbackControl()
        }



        Glide.with(this)
            .load(historyTrackClick.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.artWorkUrl100_radius_media)))
            .into(ivTitle)

        backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }

    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playOrPauseButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            time?.text = "00:00"
            mainThreadHandler?.removeCallbacksAndMessages(null)
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playOrPauseButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playOrPauseButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                mainThreadHandler?.post(
                    updateTime())
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
    private fun updateTime (): Runnable {

         return object : Runnable {
             override fun run() {
                 if (playerState == STATE_PLAYING) {
                     time?.text = SimpleDateFormat(
                         "mm:ss",
                         Locale.getDefault()
                     ).format(mediaPlayer.currentPosition)
                     mainThreadHandler?.postDelayed(this, DELAY)

                 } else {
                     mainThreadHandler?.removeCallbacks(this, DELAY)
                 }
                 
             }

         }
    }


}



