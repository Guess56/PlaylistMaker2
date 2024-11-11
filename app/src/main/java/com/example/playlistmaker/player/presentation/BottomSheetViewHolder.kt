package com.example.playlistmaker.player.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity

class BottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playListName: TextView = itemView.findViewById(R.id.tvNamePlayList)
    private val image: ImageView = itemView.findViewById(R.id.ivPlayList)
    private val playListCount: TextView = itemView.findViewById(R.id.countPlayList)

    fun bind(item: PlayListEntity, onItemClickListener : OnItemClickListenerBS?) {
        val countList :String = item.count.toString().plus(" ").plus(checkCount(item.count))

        playListName.text = item.namePlayList
        playListCount.text = countList

        Glide.with(itemView)
            .load(item.image)
            .placeholder(R.drawable.plaseholder_playlist)
            .transform(CenterCrop(),RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playListImage_radius)))
            .into(image)

        itemView.setOnClickListener{
            onItemClickListener?.onItemClick(item)
        }
    }

    fun interface OnItemClickListenerBS {
        fun onItemClick(item: PlayListEntity)
    }

    fun checkCount(count:Int): String{
        var word: String
        val countTrack = count % 100 / 10
        if (countTrack == 1){
            word = "Треков"
        }
        when(count % 10){
            1 -> word = "Трек"
            2,3,4 ->  word ="Трека"
            else -> word ="Треков"
        }
        return word
    }
}
