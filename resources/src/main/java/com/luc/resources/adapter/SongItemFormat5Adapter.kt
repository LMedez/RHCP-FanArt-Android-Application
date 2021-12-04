package com.luc.resources.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.SongMetadata
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.databinding.SongItemFormat3Binding
import com.luc.resources.databinding.SongItemFormat5Binding

class SongItemFormat5Adapter : BaseSongAdapter(R.layout.song_item_format_5) {
    override val differ = AsyncListDiffer(this, diffCallback)
    private var onPlayListener: ((SongMetadata) -> Unit)? = null

    fun setOnPlayListener(listener: (SongMetadata) -> Unit) {
        onPlayListener = listener
    }
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = holder.binding as SongItemFormat5Binding
        val song = songs[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }

            playButton.setOnClickListener {
                onPlayListener?.let { click ->
                    click(song)
                }
            }

            albumName.text = song.title.capitalizeFirstChar()
            songName.text = song.albumName.capitalizeFirstChar()
            albumImage.loadUrl(song.imageUrl)
        }
    }
}