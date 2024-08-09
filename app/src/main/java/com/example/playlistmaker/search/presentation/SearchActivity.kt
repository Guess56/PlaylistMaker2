package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.MediaPlayer
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.ViewModel.TrackHistoryViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {

    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        const val KEY = "key"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private var editValue: String = AMOUNT_DEF
    lateinit var inputEditText: EditText
    lateinit var imageError: ImageView
    lateinit var placeholderMessage: TextView
    lateinit var refresh: Button
    lateinit var rvHistory: RecyclerView
    lateinit var rvTrack: RecyclerView
    lateinit var clearHistory: Button
    lateinit var historyLayout: ViewGroup
    lateinit var adapterHistory: TrackAdapter
    lateinit var tv_search: TextView
    lateinit var progressBar: ProgressBar
    lateinit var viewModelSearch: TrackSearchViewModel


    private val track = ArrayList<Track>()
    private var trackSearch = listOf<Track>()
    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())




    private val viewModel by lazy {
        ViewModelProvider(this)[TrackHistoryViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        clearHistory = findViewById<Button>(R.id.ClearHistory)
        imageError = findViewById(R.id.iv_Error)
        placeholderMessage = findViewById(R.id.tv_Error)
        refresh = findViewById(R.id.Refresh)
        rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        historyLayout = findViewById(R.id.HistoryLayout)
        inputEditText = findViewById(R.id.editTextSearch)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        tv_search = findViewById(R.id.tv_searchHistory)
        val backButton = findViewById<Toolbar>(R.id.toolbarSearch)

        viewModelSearch = ViewModelProvider(
            this,
            TrackSearchViewModel.getViewModelFactory()
        )[TrackSearchViewModel::class.java]


        adapterHistory = TrackAdapter()

        viewModelSearch.getScreenState().observe(this){ state ->
            when(state){
                is TrackSearchState.Loading -> {
                showLoading()
                }
                is TrackSearchState.ContentHistory -> {
                    showHistory(state.data)
                }
                is TrackSearchState.Error -> {
                    showError()
                }
                is TrackSearchState.Content ->{
                    if (state.data.isNotEmpty()) {
                        showTrackList(state.data)
                    } else showEmpty()
                }
            }
        }

        adapterHistory.updateItems(trackSearch)

        adapter.notifyDataSetChanged()
        rvHistory.adapter = adapterHistory
        val clearButton = findViewById<ImageView>(R.id.clearIcon)


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackSearch.isNotEmpty()) {
                showHistoryMessage()
            } else
                showTrackListMessage()
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`inputEditText`.windowToken, 0)
            track.clear()
            adapter.updateItems(track)
            adapter.notifyDataSetChanged()
            refresh.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            imageError.visibility = View.GONE
            if (trackSearch.isEmpty()) {
                historyLayout.visibility = View.GONE
            } else historyLayout.visibility = View.VISIBLE
        }

        var inputText=""
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !p0.isNullOrEmpty()

                if (inputEditText.hasFocus() && p0.toString().isEmpty()) {
                    historyLayout.visibility = View.GONE
                    rvTrack.visibility = View.GONE
                    imageError.visibility = View.GONE
                    placeholderMessage.visibility = View.GONE
                    refresh.visibility = View.GONE
                } else {

                    inputText = p0.toString()
                    showTrackListMessage()
                    viewModelSearch.searchDebounce(p0.toString())


                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                viewModelSearch.searchDebounce(inputText)

                true
            }
            false
        }

        
        inputEditText.addTextChangedListener(simpleTextWatcher)

        refresh.setOnClickListener {
            viewModelSearch.searchDebounce(inputText)
        }

        adapter.updateItems(track)


        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapterHistory



        adapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            openMedia(track)

            trackSearch = viewModel.checkHistory(track)
            historyLayout.visibility = View.VISIBLE
            adapterHistory.updateItems(trackSearch)
            rvHistory.adapter = adapterHistory
        }

        adapterHistory.onItemClickListener = TrackViewHolder.OnItemClickListener { trackSearch ->
            openMedia(trackSearch)
        }

        clearHistory.setOnClickListener {
            viewModel.clearHistory()
            historyLayout.visibility = View.GONE
            trackSearch = emptyList()
            adapterHistory.updateItems(trackSearch)
            adapter.notifyDataSetChanged()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        editValue = inputEditText.text.toString()
        outState.putString(PRODUCT_AMOUNT, editValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
        inputEditText.setText(editValue)
    }

    private fun showLoading(){
        progressBar.isVisible = true
        rvTrack.isVisible = false
        placeholderMessage.isVisible = false
        imageError.isVisible = false
        refresh.isVisible = false
    }
    private fun showTrackList(tracks: List<Track>){
        track.clear()
        track.addAll(tracks)
        adapter.updateItems(tracks)
        adapter.notifyDataSetChanged()
        rvTrack.isVisible = true
        progressBar.isVisible = false
        placeholderMessage.isVisible = false
    }
    private fun showHistory(tracks: List<Track>) {
        adapterHistory.updateItems(tracks)
        historyLayout.isVisible = true
        rvTrack.isVisible = false
        placeholderMessage.isVisible = false
    }
    private fun showError(){
        placeholderMessage.text = getString(R.string.something_went_wrong)
        imageError.setImageResource(R.drawable.connect)
        imageError.isVisible = true
        placeholderMessage.isVisible = true
        refresh.isVisible = true
        rvTrack.isVisible = false
        progressBar.isVisible = false
        historyLayout.isVisible = false
    }

    private fun showEmpty(){
        placeholderMessage.text = getString(R.string.nothing_found)
        imageError.setImageResource(R.drawable.nothing_found)
        placeholderMessage.isVisible = true
        imageError.isVisible = true
        progressBar.isVisible = false
        rvTrack.isVisible = false
        historyLayout.isVisible = false
        refresh.isVisible = false
    }
    private fun showTrackListMessage(){
        placeholderMessage.isVisible = false
        historyLayout.isVisible = false
        rvTrack.isVisible = true
    }
    private fun showHistoryMessage(){
        placeholderMessage.isVisible = false
        historyLayout.isVisible = true
        rvTrack.isVisible = false
    }

    fun openMedia(track: Track) {
        val itemMedia = track
        if (clickDebounce()) {
            val mediaIntent = Intent(this, MediaPlayer::class.java)
            val gson = Gson()
            val json = gson.toJson(itemMedia)
            mediaIntent.putExtra(KEY, json)
            startActivity(mediaIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}

