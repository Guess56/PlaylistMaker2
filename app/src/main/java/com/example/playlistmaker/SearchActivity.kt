package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
    val track = ArrayList<Track>()
    val adapter = TrackAdapter(track)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<Toolbar>(R.id.toolbarSearch)
        imageError = findViewById(R.id.iv_Error)
        placeholderMessage = findViewById(R.id.tv_Error)
        refresh = findViewById(R.id.Refresh)


        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        inputEditText = findViewById(R.id.editTextSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`inputEditText`.windowToken, 0)
            track.clear()
            adapter.notifyDataSetChanged()
            refresh.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            imageError.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
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

        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

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
            itunesAPI.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>
                ){
                    if (response.code() == 200) {
                        track.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            track.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            imageError.visibility = View.GONE
                            refresh.visibility = View.GONE
                        }
                        if (track.isEmpty()) {
                            placeholderMessage.text = getString(R.string.nothing_found)
                            placeholderMessage.visibility=View.VISIBLE
                            imageError.setImageResource(R.drawable.nothing_found)
                            imageError.visibility = View.VISIBLE
                            refresh.visibility = View.GONE
                        } else {
                            placeholderMessage.text = getString(R.string.something_went_wrong)
                            imageError.setImageResource(R.drawable.connect)
                            refresh.isVisible = true
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {

                    placeholderMessage.text = getString(R.string.something_went_wrong)
                    imageError.visibility = View.VISIBLE
                    placeholderMessage.visibility=View.VISIBLE
                    imageError.setImageResource(R.drawable.connect)
                    refresh.isVisible = true

                }

            })
        }
}
}

