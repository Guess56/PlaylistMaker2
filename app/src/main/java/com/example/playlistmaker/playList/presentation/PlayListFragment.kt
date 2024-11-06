package com.example.playlistmaker.playList.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TabMediaFragmentBinding
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.presentation.playListViewModel.CreatePlayListViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListFragmentViewModel
import com.example.playlistmaker.playList.presentation.playListViewModel.PlayListState
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.models.Track
import com.markodevcic.peko.PermissionRequester
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {
    companion object {
        fun newInstance() = PlayListFragment()
    }
    private var _binding: TabMediaFragmentBinding? = null
    lateinit var rvPlayList: RecyclerView


    private val playList = ArrayList<PlayListEntity>()


    private val viewModelCreatePlayList by viewModel<CreatePlayListViewModel>()
    private val viewModelPlayList by viewModel<PlayListFragmentViewModel>()
    val binding: TabMediaFragmentBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = TabMediaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val createPlaylist = binding.createPlayList
        rvPlayList = binding.rvPlayList
        rvPlayList.layoutManager = GridLayoutManager(requireContext(),2)

        viewModelCreatePlayList.getPlayList()

       viewModelCreatePlayList.getPlayListState().observe(viewLifecycleOwner){ state->
           when(state){
               is PlayListState.Error -> {
                showEmpty()
               }
               is PlayListState.Content ->{
                   showPlayList()

                   val playListAdapter = PlayListAdapter(state.data)
                   rvPlayList.adapter = playListAdapter
                   playListAdapter.notifyDataSetChanged()
               }
           }

       }



        createPlaylist.setOnClickListener {
            it.findNavController().navigate(R.id.createPlayListFragment)
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    private fun showEmpty(){

    }

    private fun showPlayList(){
        rvPlayList.isVisible = true
    }


}