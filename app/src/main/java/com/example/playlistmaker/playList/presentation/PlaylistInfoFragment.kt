package com.example.playlistmaker.playList.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListFragmentBinding
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.playList.presentation.playListViewModel.CreatePlayListViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.ListPlayListState
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListIdState
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListInfoViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListState
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListTrackGetState
import com.example.playlistmaker.player.presentation.MediaPlayer
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchFragment
import com.example.playlistmaker.search.presentation.TrackAdapter
import com.example.playlistmaker.search.presentation.TrackViewHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Collections.replaceAll
import java.util.Date
import java.util.Locale


class PlaylistInfoFragment():Fragment() {
    companion object {
        const val PLAYLIST_ID_KEY = "PLAYLIST"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val KEY = "key"
        fun newInstance(playListId: Int) = PlaylistInfoFragment().apply {
            arguments = bundleOf(PLAYLIST_ID_KEY to playListId)
        }
    }

    private val dateFormat by lazy {
        SimpleDateFormat("mm", Locale.getDefault())
    }
    private val TrackFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }


    private var _binding: PlayListFragmentBinding? = null
    val binding: PlayListFragmentBinding
        get() = _binding!!

    private val viewModel by viewModel<PlayListInfoViewModel>()
    private var isClickAllowed = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlayListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible =
            false

        val playListId = requireArguments().getInt(PLAYLIST_ID_KEY)
        val backButton = binding.toolbarSearch
        var list = ""
        var playList: PlayListEntity
        var listId: List<String> = listOf()
        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        val rvTrack = binding.rvPlayListTrackSheet
        val adapter = AdapterPlayListInfo()
        var deleteTrackId = ""
        lateinit var confirmDialog: MaterialAlertDialogBuilder

        viewModel.getPlayList(playListId)
        bottomSheetContainer.isVisible = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED



        viewModel.getPlayListState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayListIdState.Content -> {
                    playList = state.data
                    viewModel.setPlayList(playList)
                    binding.namePlayList.text = playList.namePlayList
                    binding.yearPlayList.text = playList.description
                    binding.count.text =
                        playList.count.toString().plus(" ").plus(checkCount(playList.count))

                    Glide.with(requireContext())
                        .load(playList.filePath)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imagePlayList)

                    viewModel.getTrackPlayList()
                    list = state.data.trackId
                    val new = list.replace("]", "")
                    val new2 = new.replace("[", "")
                    val new3 = new2.replace("\"", "")
                    listId = new3.split(',')
                }

                is PlayListIdState.Error -> Log.d("Sprint 23", "Нет данных")
            }
        }


        rvTrack.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adapter

        viewModel.getTrackPlayListState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayListTrackGetState.Content -> {
                    val trackId = state.data
                    val item = viewModel.checkTrack(listId, trackId)
                    viewModel.setTrackList(state.data)
                    val duration = viewModel.sumDuration(item)
                    val durationText: String = checkDuration(duration)
                    binding.duration.text = dateFormat.format(duration).plus(" ").plus(durationText)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    rvTrack.isVisible = true
                    adapter.updateItems(item)
                    adapter.notifyDataSetChanged()
                    if (item.isEmpty()){
                        Toast.makeText(requireContext(), "Плейлист пустой", Toast.LENGTH_LONG).show()
                    }
                }

                is PlayListTrackGetState.Error -> {
                    Log.d("Sprint 23", "Нет данных")
                    val durationText: String = checkDuration(0)
                    binding.duration.text = dateFormat.format(0).plus(" ").plus(durationText)
                    adapter.updateItems(emptyList())
                    adapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Плейлист пустой", Toast.LENGTH_LONG).show()
                }
            }
        }
        backButton.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        adapter.onItemClickListener = PlayListInfoViewHolder.OnItemClickListener { track ->
            openMedia(track)
        }
        adapter.OnItemClickLongListener = PlayListInfoViewHolder.OnItemClickLongListener { track ->
            confirmDialog.show()
            deleteTrackId = track.trackId.toString()
        }
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("Нет") { dialog, which ->
            }
            .setPositiveButton("Да") { dialog, which ->
                viewModel.delete(deleteTrackId, listId, playListId)
                viewModel.getPlayList(playListId)
                viewModel.getTrackPlayListState()
                adapter.notifyDataSetChanged()
            }

        binding.sharePlayList.setOnClickListener {
            val itemPlayList = viewModel.sharePlayList(playListId)
            val itemTrackList = viewModel.shareTrackList(createTracksFromJson(itemPlayList.trackId))
            if (createTracksFromJson(itemPlayList.trackId).isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    " «В этом плейлисте нет списка треков, которым можно поделиться»",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                intentShare(itemPlayList,itemTrackList)
            }
        }

        binding.optionPlayList.setOnClickListener{
            val itemPlayList = viewModel.sharePlayList(playListId)
            val itemTrackList = viewModel.shareTrackList(createTracksFromJson(itemPlayList.trackId))
            viewModel.getPlayListState()
            rvTrack.isVisible = false
            binding.rootLayout.isVisible = true
            binding.ivPlayList.isVisible = true
            binding.tvNamePlayList.isVisible = true
            binding.countPlayList.isVisible = true
            binding.sheetShare.isVisible = true
            binding.sheetRedactor.isVisible = true
            binding.sheetDelete.isVisible = true

            Glide.with(requireContext())
                .load(itemPlayList.filePath)
                .placeholder(R.drawable.placeholder)
                .into(binding.ivPlayList)
            binding.tvNamePlayList.text = itemPlayList.namePlayList
            binding.countPlayList.text = itemPlayList.count.toString().plus(" ").plus(checkCount(itemPlayList.count))


            binding.sheetShare.setOnClickListener{
                val itemPlayList = viewModel.sharePlayList(playListId)
                val itemTrackList = viewModel.shareTrackList(createTracksFromJson(itemPlayList.trackId))

                if (createTracksFromJson(itemPlayList.trackId).isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        " «В этом плейлисте нет списка треков, которым можно поделиться»",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    intentShare(itemPlayList,itemTrackList)
                }
            }

            binding.sheetRedactor.setOnClickListener{
                it.findNavController().navigate(R.id.action_playlistInfoFragment_to_redactorPlayListFragment,Bundle().apply {
                    putInt("PLAYLISTID",itemPlayList.playListId)
                })
            }

            confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удалить плейлист?")
                .setMessage("Хотите удалить плейлист «${itemPlayList.namePlayList}»?")
                .setNegativeButton("Нет") { dialog, which ->
                }
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deleteAllTrackPlayList(createTracksFromJson(itemPlayList.trackId))
                    viewModel.deletePlayList(itemPlayList)
                    findNavController().navigate(R.id.action_playlistInfoFragment_to_mediaFragment)

                }

            binding.sheetDelete.setOnClickListener{
                confirmDialog.show()
            }

        }
    }
    fun intentShare(itemPlayList:PlayListEntity,itemTrackList:List<PlayListTrackEntity>){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            itemTrackList.formatToStringSharing(itemPlayList)
        )
        shareIntent.setType("text/plain")
        val intentChooser = Intent.createChooser(shareIntent, "")
        startActivity(intentChooser)
    }

    fun createTracksFromJson(json: String): ArrayList<String> {
        if (json == "") return ArrayList()
        val trackListType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, trackListType)
    }


    fun checkCount(count: Int): String {
        var word: String
        val countTrack = count % 100 / 10
        if (countTrack == 1) {
            word = "треков"
        }
        when (count % 10) {
            1 -> word = "трек"
            2, 3, 4 -> word = "трека"
            else -> word = "треков"
        }
        return word
    }

    fun checkDuration(count: Int): String {
        var word: String
        val countTrack = count % 100 / 10
        if (countTrack == 1) {
            word = "минут"
        }
        when (count % 10) {
            1 -> word = "минут"
            2, 3, 4 -> word = "минуты"
            else -> word = "минут"
        }
        return word
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible =
            true
    }

    fun openMedia(track: PlayListTrackEntity) {
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
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlayListState()
        viewModel.getTrackPlayListState()
    }

    fun List<PlayListTrackEntity>.formatToStringSharing(playlist: PlayListEntity): String {
        val sharingString =
            StringBuilder().append(
                "${playlist.namePlayList}\n" +
                        "${playlist.description}\n" +
                        "${this.size} ${checkCount(this.size)} \n"
            )
        this.forEachIndexed { index, track ->
            val artist = track.artistName
            val name = track.trackName
            val duration = TrackFormat.format(track.trackTimeMillis).plus(" ").plus(checkDuration(track.trackTimeMillis))
            sharingString.append("${index + 1}. $artist - $name ($duration)\n")
        }
        return sharingString.toString()
    }
}

