package com.example.playlistmaker.playList.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity

class AdapterPlayListInfo(): RecyclerView.Adapter<PlayListInfoViewHolder> () {

    var onItemClickListener: PlayListInfoViewHolder.OnItemClickListener? = null
    var OnItemClickLongListener:PlayListInfoViewHolder.OnItemClickLongListener? = null


    private var items: List<PlayListTrackEntity> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return PlayListInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlayListInfoViewHolder, position: Int) {
        holder.bind(
            items[position],
            onItemClickListener = onItemClickListener,
            OnItemClickLongListener = OnItemClickLongListener
        )
    }

    fun updateItems(newItems: List<PlayListTrackEntity>) {
        val oldItems = items
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackId == newItems[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}

