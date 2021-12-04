package com.luc.resources.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.SongMetadata
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.databinding.SongItemFormat1Binding

class SongItemFormat1Adapter : BaseSongAdapter(R.layout.song_item_format_1) {
    override val differ = AsyncListDiffer(this, diffCallback)

    private var onPlayListener: ((SongMetadata) -> Unit)? = null

    fun setPlayListener(listener: (SongMetadata) -> Unit) {
        onPlayListener = listener
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = holder.binding as SongItemFormat1Binding
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
            songTitle.text = song.title.capitalizeFirstChar()
            songImage.loadUrl(song.imageUrl)
            songDuration.text = song.songDuration
            songAlbum.text = song.albumName.capitalizeFirstChar()
        }
    }
}