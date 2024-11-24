package com.example.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.databinding.TabMediaFragmentBinding
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class MediaFragment : Fragment() {
    private companion object {
        const val KEY = "key"
    }


    private var _binding: FragmentMediaBinding? = null
    val binding: FragmentMediaBinding
        get() = _binding!!


    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = binding.toolbarMedia

        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Избранные"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        _binding = null
        tabMediator.detach()
        super.onDestroyView()

    }
}
