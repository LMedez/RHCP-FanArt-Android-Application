package com.luc.home.overviewfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.AlbumMetadata
import com.luc.home.databinding.ItemPopularAlbumsBinding
import com.luc.resources.animation.loadUrl
import java.util.*
import android.view.animation.Animation

import android.view.animation.ScaleAnimation

class PopularAlbumsRecyclerViewAdapter constructor(
    private val showList: List<AlbumMetadata>,
    private val onItemClicked: (AlbumMetadata) -> Unit,
) :
    RecyclerView.Adapter<PopularAlbumsRecyclerViewAdapter.AlbumsRecyclerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularAlbumsBinding.inflate(layoutInflater, parent, false)

        return AlbumsRecyclerViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AlbumsRecyclerViewHolder, position: Int) {
        val album = showList[position]

        holder.bind(holder.itemView.context, album)

        holder.itemView.setOnClickListener { onItemClicked(album) }

    }

    override fun getItemCount() = showList.size

    inner class AlbumsRecyclerViewHolder(private val binding: ItemPopularAlbumsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, album: AlbumMetadata) = with(binding) {
            albumTitle.text = "${album.title} | ${album.trackCount} Songs".capitalizeFirstChar()
            albumImage.loadUrl(album.imageUrl)
        }
    }
}