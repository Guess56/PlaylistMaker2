package com.example.playlistmaker.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.FragmentMenuBinding
import com.example.playlistmaker.media.presentation.MediaFragment
import com.example.playlistmaker.search.presentation.SearchFragment
import com.example.playlistmaker.setting.presentation.SettingsFragment


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val optionButton = binding.option
        val searchButton = binding.search
        val mediaButton = binding.media
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
