package com.luc.resources.adapter

import android.util.Log
import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.SongMetadata
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.databinding.SongItemFormat4Binding

class SongItemFormat4Adapter : BaseSongAdapter(R.layout.song_item_format_4) {
    override val differ = AsyncListDiffer(this, diffCallback)

    private var onRemoveListener: ((SongMetadata) -> Unit)? = null

    fun setRemoveListener(listener: (SongMetadata) -> Unit) {
        onRemoveListener = listener
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = holder.binding as SongItemFormat4Binding
        val song = songs[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
            remove.setOnClickListener {
                onRemoveListener?.let { click ->
                    click(song)
                }
            }
            songTitle.text = song.title.capitalizeFirstChar()
            songAlbum.text = song.albumName.capitalizeFirstChar()
            songImage.loadUrl(song.imageUrl)
        }
    }
}