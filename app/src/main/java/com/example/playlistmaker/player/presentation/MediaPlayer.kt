package com.example.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SheetBinding
import com.example.playlistmaker.favorite.presentation.viewModel.FavoriteViewModel
import com.example.playlistmaker.media.presentation.MediaFragment
import com.example.playlistmaker.playList.presentation.PlayListAdapter
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListState
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchFragment
import com.example.playlistmaker.search.presentation.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayer : AppCompatActivity() {
    companion object {
        private const val KEY = "key"
    }

    private val mediaPlayer = MediaPlayer()
    private lateinit var playOrPauseButton: ImageView

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
    private val viewModel by viewModel<MediaPlayerViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sheet)
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
        val tvTime = findViewById<TextView>(R.id.time)
        tvTrackName.text = historyTrackClick.trackName
        tvAlbumName.text = historyTrackClick.artistName
        tvCountry.text = historyTrackClick.country
        tvGenre.text = historyTrackClick.primaryGenreName
        tvAlbum.text = historyTrackClick.collectionName
        tvYears.text = viewModel.formatReleaseDate(historyTrackClick.releaseDate)
        tvDuration?.text = dateFormat.format(historyTrackClick.trackTimeMillis)
        val url = historyTrackClick.previewUrl
        val imageLike = findViewById<ImageView>(R.id.like)
        val imageAdd = findViewById<ImageView>(R.id.addTrack)
        val bottomSheetContainer = findViewById<LinearLayout>(R.id.standard_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        val rvPlayListSheet = findViewById<RecyclerView>(R.id.rvPlayListSheet)
        lateinit var adapterPlayListSheet: BottomSheetAdapter



        viewModel.getPlayListState().observe(this){ state->
            when(state){
                is PlayListState.Error -> {
                }
                is PlayListState.Content ->{
                    showPlayList()
                    adapterPlayListSheet = BottomSheetAdapter()
                    adapterPlayListSheet.updateItems(state.data)
                    rvPlayListSheet.adapter = adapterPlayListSheet
                    adapterPlayListSheet.notifyDataSetChanged()
                }
            }

        }
        viewModel.getPlayList()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        playOrPauseButton = findViewById<ImageView>(R.id.playOrPause)

        viewModel.preparePlayer(url)

        playOrPauseButton.setOnClickListener {
            playbackControl()
        }
        viewModel.checkState(historyTrackClick)

        imageLike.setOnClickListener{
             viewModel.onFavoriteClicked(historyTrackClick)
        }

        viewModel.inFavorite().observe(this) { imageState ->
            when (imageState) {
                false -> imageLike.setImageResource(R.drawable.like)
                true -> imageLike.setImageResource(R.drawable.like_red)
            }
        }






        viewModel.state.observe(this) { state ->
            when (state) {

                MediaPlayerViewModel.STATE_PLAYING -> playOrPauseButton.setImageResource(R.drawable.pause)
                MediaPlayerViewModel.STATE_PAUSED, MediaPlayerViewModel.STATE_PREPARED ->{
                    playOrPauseButton.setImageResource(R.drawable.play)
                }
                MediaPlayerViewModel.STATE_COMPLETE -> {
                    playOrPauseButton.setImageResource(R.drawable.play)
                }
            }
        }



        Glide.with(this)
            .load(viewModel.formatArtworkUrl(historyTrackClick.artworkUrl100))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.artWorkUrl100_radius_media)))
            .into(ivTitle)

        backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        imageAdd.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        viewModel.info.observe(this, Observer { info ->
            tvTime?.text = info.currentPosition
        })
    }



    private fun playbackControl() {
        when (viewModel.state.value) {
            MediaPlayerViewModel.STATE_PLAYING -> viewModel.pausePlayer()
            MediaPlayerViewModel.STATE_PREPARED, MediaPlayerViewModel.STATE_PAUSED -> viewModel.startPlayer()
            MediaPlayerViewModel.STATE_COMPLETE -> viewModel.startPlayer()
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer.release()
        }
    private fun showPlayList(){

    }
}



