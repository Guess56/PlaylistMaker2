package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.domain.models.Track
import com.example.playlistmaker.Search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.player.presentation.MediaPlayerViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayer : AppCompatActivity() {
    companion object {
         const val KEY = "key"
         const val STATE_DEFAULT = 0
         const val STATE_PREPARED = 1
         const val STATE_PLAYING = 2
         const val STATE_PAUSED = 3

    }

    private val mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT
    private lateinit var playOrPauseButton: ImageView
    private var mainThreadHandler: Handler? = null
    private lateinit var viewModel: MediaPlayerViewModel
    private var time : TextView? = null
    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val backButton = findViewById<Toolbar>(R.id.toolbarBack)
        viewModel = ViewModelProvider(
            this,
            MediaPlayerViewModel.getViewModelFactory()
        )[MediaPlayerViewModel::class.java]


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
        val tvTime = findViewById<TextView>(R.id.time)
        tvTrackName.text = historyTrackClick.trackName
        tvAlbumName.text = historyTrackClick.artistName
        tvCountry.text = historyTrackClick.country
        tvGenre.text = historyTrackClick.primaryGenreName
        tvAlbum.text = historyTrackClick.collectionName
        tvYears.text = viewModel.formatReleaseDate(historyTrackClick.releaseDate)
        tvDuration?.text = dateFormat.format(historyTrackClick.trackTimeMillis)
        val url = historyTrackClick.previewUrl




        mainThreadHandler = Handler(Looper.getMainLooper())


         playOrPauseButton = findViewById<ImageView>(R.id.playOrPause)

       viewModel.preparePlayer(url)

        playOrPauseButton.setOnClickListener {
            playbackControl()
        }

        viewModel.state.observe(this, { state ->
            when (state) {
                MediaPlayerViewModel.STATE_PLAYING -> playOrPauseButton.setImageResource(R.drawable.pause)
                MediaPlayerViewModel.STATE_PAUSED, MediaPlayerViewModel.STATE_PREPARED -> playOrPauseButton.setImageResource(
                    R.drawable.play
                )

                MediaPlayerViewModel.STATE_COMPLETE -> {
                    playOrPauseButton.setImageResource(R.drawable.play)
                }
            }
        })




        Glide.with(this)
            .load(viewModel.formatArtworkUrl(historyTrackClick.artworkUrl100))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.artWorkUrl100_radius_media)))
            .into(ivTitle)

        backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.info.observe(this, Observer { info ->
            tvTime?.text =info.currentPosition
        })



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
        when(viewModel.state.value) {
            MediaPlayerViewModel.STATE_PLAYING -> viewModel.pausePlayer()
            MediaPlayerViewModel.STATE_PREPARED, MediaPlayerViewModel.STATE_PAUSED -> viewModel.startPlayer()
            MediaPlayerViewModel.STATE_COMPLETE -> viewModel.startPlayer()

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
}



