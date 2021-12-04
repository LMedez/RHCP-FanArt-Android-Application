package com.luc.resources.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.AlbumMetadata
import com.luc.resources.R
import com.luc.resources.animation.loadUrl
import com.luc.resources.animation.scaleXY
import com.luc.resources.animation.startFavAnimation
import com.luc.resources.databinding.AlbumItemFormat2Binding

class AlbumItemFormat2Adapter : BaseAlbumAdapter(R.layout.album_item_format_2) {

    override val differ = AsyncListDiffer(this, diffCallback)

    var favAlbumList: List<AlbumMetadata> = listOf()

    private var onAlbumPlayClickListener: ((AlbumMetadata) -> Unit)? = null

    fun setAlbumPlayClickListener(listener: (AlbumMetadata) -> Unit) {
        onAlbumPlayClickListener = listener
    }

    private var onFavListener: ((AlbumMetadata) -> Unit)? = null

    fun setFavListener(listener: (AlbumMetadata) -> Unit) {
        onFavListener = listener
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val binding = holder.binding as AlbumItemFormat2Binding
        val album = albumList[position]
        with(binding) {
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(album)
                }
            }

            favBorderButton.setOnClickListener {
                favButton.startFavAnimation()
                onFavListener?.let { click ->
                    click(album)
                }
            }

            if (favAlbumList.contains(album)) {
                favButton.scaleXY(1f)
            } else favButton.scaleXY(0f)

            playButton.setOnClickListener {
                onAlbumPlayClickListener?.let { click ->
                    click(album)
                }
            }
            albumImage.loadUrl(album.imageUrl)
            albumTitle.text = album.title.capitalizeFirstChar()
            artistName.text = album.artist.capitalizeFirstChar()
        }
    }
}