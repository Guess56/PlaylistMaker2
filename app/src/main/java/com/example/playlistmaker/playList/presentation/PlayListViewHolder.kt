package com.example.playlistmaker.playList.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.TrackViewHolder

class PlayListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    private val playListName: TextView = itemView.findViewById(R.id.playListName)
    private val image: ImageView = itemView.findViewById(R.id.playListImage)
    private val playListCount: TextView = itemView.findViewById(R.id.countTrack)
    fun bind(item: PlayListEntity) {

        playListName.text = item.namePlayList
        playListCount.text = item.count.toString()

        Glide.with(itemView)
            .load(item.image)
            .placeholder(R.drawable.plaseholder_playlist)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playListImage_radius)))
            .into(image)
    }
}
    ///дописать формат слова в зависимости от числа

