package com.example.playlistmaker.player.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

class BottomSheetAdapter(): RecyclerView.Adapter<BottomSheetViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_list_items_media_player, parent, false)
        return BottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(items[position],onItemClickListener = onItemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    var onItemClickListener: BottomSheetViewHolder.OnItemClickListenerBS? = null

    private var items: List<PlayListEntity> = emptyList()
    fun updateItems(newItems: List<PlayListEntity>) {
        val oldItems = items
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].playListId == newItems[newItemPosition].playListId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}

