package com.luc.resources.adapter

import android.util.Log
import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.SongMetadata
import com.luc.resources.R
import com.luc.resources.animation.scaleXY
import com.luc.resources.animation.startFavAnimation
import com.luc.resources.animation.startSwapTextToImageViewAnimation
import com.luc.resources.databinding.SongItemFormat2Binding

class SongItemFormat2Adapter : BaseSongAdapter(R.layout.song_item_format_2) {

    override val differ = AsyncListDiffer(this, diffCallback)

    var favSongList: List<SongMetadata> = listOf()

    var curSong: SongMetadata? = null
        set(value) {
            notifyItemChanged(songs.indexOf(curSong))
            notifyItemChanged(songs.indexOf(value))
            field = value
        }

    private var onFavListener: ((SongMetadata) -> Unit)? = null

    fun setFavListener(listener: (SongMetadata) -> Unit) {
        onFavListener = listener
    }

    private var onPlaylistListener: ((SongMetadata) -> Unit)? = null

    fun setPlaylistListener(listener: (SongMetadata) -> Unit) {
        onPlaylistListener = listener
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val binding = holder.binding as SongItemFormat2Binding
        val song = songs[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }

            favBorderButton.setOnClickListener {
                favButton.startFavAnimation()
                onFavListener?.let { click ->
                    click(song)
                }
            }

            playlistAddButton.setOnClickListener {
                onPlaylistListener?.let { click ->
                    click(song)
                }
            }

            if (favSongList.contains(song)) {
                favButton.scaleXY(1f)
            } else favButton.scaleXY(0f)

            if (curSong != null && curSong == song) {
                startSwapTextToImageViewAnimation(songTrackNum, equalizerIcon, true)
            } else {
                startSwapTextToImageViewAnimation(songTrackNum, equalizerIcon, false)
            }

            songSubtitle.text = song.albumName.capitalizeFirstChar()
            songName.text = song.title.capitalizeFirstChar()
            songTrackNum.text =
                if (song.trackNumber < 10) "0${song.trackNumber}" else song.trackNumber.toString()
        }
    }
}