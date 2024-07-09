package com.example.playlistmaker

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
        private  const val DELAY = 300L
    }

    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT
    private lateinit var playOrPauseButton: ImageView
    private var mainThreadHandler: Handler? = null
    private var time : TextView? = null



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
            .into(iv_title)

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



