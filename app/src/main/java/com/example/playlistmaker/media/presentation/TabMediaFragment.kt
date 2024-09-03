package com.example.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TabMediaFragmentBinding

class TabMediaFragment : Fragment() {
    companion object {
        fun newInstance() = TabMediaFragment()
    }

    private lateinit var binding: TabMediaFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = TabMediaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}