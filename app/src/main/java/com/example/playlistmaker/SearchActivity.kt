package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {


    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesAPI = retrofit.create(itunesAPI::class.java)

    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
    }
    private var editValue: String = AMOUNT_DEF
    private lateinit var inputEditText: EditText
    private lateinit var imageError: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var refresh: Button
    private lateinit var rvHistory: RecyclerView
    private lateinit var rvTrack: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var historyLayout: ViewGroup
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var tv_search: TextView



    val track = ArrayList<Track>()
    var trackSearch = listOf<Track>()
    val adapter = TrackAdapter()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        rvHistory= findViewById<RecyclerView>(R.id.rvHistory)
        clearHistory = findViewById<Button>(R.id.ClearHistory)
        imageError = findViewById(R.id.iv_Error)
        placeholderMessage = findViewById(R.id.tv_Error)
        refresh = findViewById(R.id.Refresh)
        rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        historyLayout = findViewById(R.id.HistoryLayout)
        inputEditText = findViewById(R.id.editTextSearch)
        tv_search = findViewById(R.id.tv_searchHistory)
        val backButton = findViewById<Toolbar>(R.id.toolbarSearch)
        val searchHistory = SearchHistory(this)

        adapterHistory = TrackAdapter()

        trackSearch = searchHistory.getTrack()

        Log.d("Sprint","focus $trackSearch")
        adapterHistory.updateItems(trackSearch)
        adapter.notifyDataSetChanged()
        rvHistory.adapter = adapterHistory
        val clearButton = findViewById<ImageView>(R.id.clearIcon)


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackSearch.isNotEmpty()){
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
            if(trackSearch.isEmpty()){
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

        refresh.setOnClickListener{
            search()
        }

        adapter.updateItems(track) ///

        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapterHistory



        adapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            trackSearch = searchHistory.checkHistory(track)
            historyLayout.visibility = View.VISIBLE
            adapterHistory.updateItems(trackSearch)
            rvHistory.adapter = adapterHistory
        }

        clearHistory.setOnClickListener{
            searchHistory.clearHistory()
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
        itunesAPI.search(inputEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        track.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            showMessage(StatusResponse.SUCCESS)
                            track.addAll(response.body()?.results!!)
                            adapter.updateItems(track)
                            adapter.notifyDataSetChanged()
                        }
                        if (track.isEmpty()) {
                            showMessage(StatusResponse.EMPTY)
                        }
                    } else {
                        showMessage(StatusResponse.ERROR)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(StatusResponse.ERROR)

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
                historyLayout.visibility = View.GONE
                imageError.visibility = View.GONE
                rvTrack.visibility = View.VISIBLE
            }

            StatusResponse.EMPTY -> {
                placeholderMessage.text = getString(R.string.nothing_found)
                imageError.setImageResource(R.drawable.nothing_found)
                refresh.visibility = View.GONE
                imageError.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                rvTrack.visibility = View.GONE
            }

            StatusResponse.ERROR -> {
                placeholderMessage.text = getString(R.string.something_went_wrong)
                imageError.setImageResource(R.drawable.connect)
                imageError.visibility = View.VISIBLE
                refresh.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                rvTrack.visibility = View.GONE
            }
        }
    }
    enum class StatusResponse{
        SUCCESS,EMPTY,ERROR
    }
    fun showHistory(){
        rvTrack.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        historyLayout.visibility = View.VISIBLE
    }

}

