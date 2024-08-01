package com.example.playlistmaker.ui.Search

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.Consumer
import com.example.playlistmaker.domain.api.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.MediaPlayer
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


    private val track = ArrayList<Track>()
    private var trackSearch = listOf<Track>()
    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }



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


        adapterHistory = TrackAdapter()
        val searchHistoryInteractor = Creator.provideHistoryInteractor()
        trackSearch = searchHistoryInteractor.getTrack()

        adapterHistory.updateItems(trackSearch)

        adapter.notifyDataSetChanged()
        rvHistory.adapter = adapterHistory
        val clearButton = findViewById<ImageView>(R.id.clearIcon)


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackSearch.isNotEmpty()) {
                showHistory()
            } else showMessage(StatusResponse.SUCCESS)
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
                    showMessage(StatusResponse.SUCCESS)
                    searchDebounce()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                search()
                true
            }
            false
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        refresh.setOnClickListener {
            search()
        }

        adapter.updateItems(track)

        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapterHistory


        adapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            openMedia(track)


            trackSearch = searchHistoryInteractor.checkHistory(track)

            historyLayout.visibility = View.VISIBLE
            adapterHistory.updateItems(trackSearch)
            rvHistory.adapter = adapterHistory
        }

        adapterHistory.onItemClickListener = TrackViewHolder.OnItemClickListener { trackSearch ->
            openMedia(trackSearch)
        }

        clearHistory.setOnClickListener {

            searchHistoryInteractor.clearHistory()

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

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            placeholderMessage.visibility = View.GONE
            rvTrack.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        val getTrack = Creator.provideTrackInteractor()
        getTrack.searchTrack(inputEditText.text.toString(), object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {
                runOnUiThread {
                    if (data is ConsumerData.Error) {
                        showMessage(StatusResponse.ERROR)
                    } else if (data is ConsumerData.Data) {
                        if (data.value.isNotEmpty() == true) {
                            showMessage(StatusResponse.SUCCESS)
                            track.addAll(data.value)
                            adapter.updateItems(track)
                            adapter.notifyDataSetChanged()
                        }
                        if (data.value.isEmpty()) {
                            showMessage(StatusResponse.EMPTY)
                        }
                    }
                }
            }
        })
    }


    private fun showMessage(status: StatusResponse) {
        placeholderMessage.isVisible = true
        track.clear()
        adapter.notifyDataSetChanged()
        when (status) {
            StatusResponse.SUCCESS -> {
                placeholderMessage.visibility = View.GONE
                progressBar.visibility = View.GONE
                historyLayout.visibility = View.GONE
                imageError.visibility = View.GONE
                rvTrack.visibility = View.VISIBLE
            }

            StatusResponse.EMPTY -> {
                placeholderMessage.text = getString(R.string.nothing_found)
                imageError.setImageResource(R.drawable.nothing_found)
                refresh.visibility = View.GONE
                progressBar.visibility = View.GONE
                imageError.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                rvTrack.visibility = View.GONE
            }

            StatusResponse.ERROR -> {
                placeholderMessage.text = getString(R.string.something_went_wrong)
                imageError.setImageResource(R.drawable.connect)
                imageError.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                refresh.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                rvTrack.visibility = View.GONE
            }
        }
    }

    enum class StatusResponse {
        SUCCESS, EMPTY, ERROR
    }

    fun showHistory() {
        rvTrack.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        historyLayout.visibility = View.VISIBLE
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

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}

