package com.example.playlistmaker.playList.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvTrackNameArtist)
    private val tvTrackTimeMillis: TextView = itemView.findViewById(R.id.tvTrackDuration)
    private val ivArtworkUrl100: ImageView = itemView.findViewById(R.id.ivTrackTitle)
    fun bind(
        item: PlayListTrackEntity,
        onItemClickListener: OnItemClickListener?,
        onItemLongClick: Unit
    ) {
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.artWorkUrl100_radius)))
            .into(ivArtworkUrl100)

        itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
        itemView.setOnLongClickListener {
            onItemLongClick.onItemClickLong(item)
        }

    }
    fun interface OnItemClickListener {
        fun onItemClick(item: PlayListTrackEntity)

    }
    fun interface OnItemLongClick{
        fun  onItemClickLong(item:PlayListTrackEntity):Boolean
    }

}
