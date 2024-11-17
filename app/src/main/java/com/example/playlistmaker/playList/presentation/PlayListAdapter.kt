package com.example.playlistmaker.playList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.player.presentation.BottomSheetViewHolder
import com.example.playlistmaker.search.presentation.TrackViewHolder

class PlayListAdapter(private val playlist:List<PlayListEntity>): RecyclerView.Adapter<PlayListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false)
        return PlayListViewHolder(view)
    }
    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playlist[position],onItemClickListener = onItemClickListener)
    }
    var onItemClickListener: PlayListViewHolder.OnItemClickListener? = null

}