package com.luc.resources.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.databinding.AlbumItemFormat3Binding

class AlbumItemFormat3Adapter : BaseAlbumAdapter(R.layout.album_item_format_3) {
    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val binding = holder.binding as AlbumItemFormat3Binding
        val album = albumList[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(album)
                }
            }
            albumImage.loadUrl(album.imageUrl)
            albumName.text = album.title.capitalizeFirstChar()
            artistName.text = album.artist.capitalizeFirstChar()
        }
    }
}