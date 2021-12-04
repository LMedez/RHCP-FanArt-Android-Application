package com.luc.resources.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.databinding.SongItemFormat3Binding

class SongItemFormat3Adapter : BaseSongAdapter(R.layout.song_item_format_3) {
    override val differ = AsyncListDiffer(this, diffCallback)

    var listOfDates = listOf<String>()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = holder.binding as SongItemFormat3Binding
        val song = songs[position]
        val date = listOfDates[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
            songTitle.text = song.title.capitalizeFirstChar()
            songAlbum.text = song.albumName.capitalizeFirstChar()
            songTime.text = date
            songImage.loadUrl(song.imageUrl)
        }
    }
}