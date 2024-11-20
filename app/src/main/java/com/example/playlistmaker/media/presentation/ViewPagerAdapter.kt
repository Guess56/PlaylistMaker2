package com.example.playlistmaker.media.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.favorite.presentation.FavoriteFragment
import com.example.playlistmaker.playList.presentation.PlayListFragment


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> FavoriteFragment.newInstance()
            else -> PlayListFragment.newInstance()


        }
    }
}