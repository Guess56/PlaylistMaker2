package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.media.presentation.MediaFragment
import com.example.playlistmaker.player.presentation.MediaPlayer
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.ViewModel.TrackHistoryViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.search.presentation.state.TrackSearchState
import com.google.gson.Gson
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding : FragmentSearchBinding
        get() = _binding!!

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
    private val viewModelSearch by viewModel<TrackSearchViewModel>()
    private val viewModel by viewModel<TrackHistoryViewModel>()


    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        const val KEY = "key"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = binding.rvHistory
        clearHistory = binding.ClearHistory
        imageError = binding.ivError
        placeholderMessage = binding.tvError
        refresh = binding.Refresh
        rvTrack = binding.rvTrack
        historyLayout = binding.HistoryLayout
        inputEditText = binding.editTextSearch
        progressBar = binding.progressBar
        tv_search = binding.tvSearchHistory

        val clearButton = binding.clearIcon


        adapterHistory = TrackAdapter()

        viewModelSearch.getScreenState().observe(viewLifecycleOwner){ state ->
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

        if (inputEditText.text.isEmpty()){
            trackSearch = viewModel.getHistory()
            if (trackSearch.isEmpty()) {
                historyLayout.visibility = View.GONE
            } else {
                historyLayout.visibility = View.VISIBLE
                showHistory(trackSearch)
            }
        }


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackSearch.isNotEmpty()) {
                showHistoryMessage()
            } else
                showTrackListMessage()
        }


        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                    historyLayout.visibility = View.VISIBLE
                    rvTrack.visibility = View.GONE
                    imageError.visibility = View.GONE
                    placeholderMessage.visibility = View.GONE
                    refresh.visibility = View.GONE
                    viewModelSearch.searchDebounce(p0.toString())
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
            viewModelSearch.refreshSearch(inputText)
        }

        adapter.updateItems(track)


        rvTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapterHistory



        adapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            openMedia(track)
            trackSearch = viewModel.checkHistory(track)
            adapterHistory.updateItems(trackSearch)
            rvHistory.adapter = adapterHistory
        }

        adapterHistory.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
             openMedia(track)
            trackSearch = viewModel.checkHistory(track)
            adapterHistory.updateItems(trackSearch)
            rvHistory.adapter = adapterHistory

        }

        clearHistory.setOnClickListener {
            viewModel.clearHistory()
            historyLayout.visibility = View.GONE
            trackSearch = emptyList()
            adapterHistory.updateItems(trackSearch)
            adapter.notifyDataSetChanged()
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        editValue = inputEditText.text.toString()
        outState.putString(PRODUCT_AMOUNT, editValue)
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

        refresh.isVisible = false
        imageError.isVisible = false

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
            val mediaIntent = Intent(requireContext(), MediaPlayer::class.java)
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

