package com.example.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TabFavoritesFragmentBinding

class TabFavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = TabFavoriteFragment()
    }

    private lateinit var binding: TabFavoritesFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = TabFavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}