package com.luc.mymusic.playlistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luc.common.entities.relations.PlaylistWithSongs
import com.luc.mymusic.databinding.ItemPlaylistBinding
import com.luc.resources.animation.loadUrl

class PlayListAdapter(
) : ListAdapter<PlaylistWithSongs, PlayListAdapter.SongRecyclerViewHolder>(DiffCallback) {

    private var onItemClickListener: ((PlaylistWithSongs, Int) -> Unit)? = null

    fun setItemClickListener(listener: (PlaylistWithSongs, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlayListAdapter.SongRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaylistBinding.inflate(layoutInflater, parent, false)
        val holder = SongRecyclerViewHolder(binding)

        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            onItemClickListener?.let { click ->
                click(getItem(position), position)
            }
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: SongRecyclerViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    inner class SongRecyclerViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistWithSongs) = with(binding) {
            if (playlist.songs.size >= 4) {
                image1.loadUrl(playlist.songs[0].imageUrl)
                image2.loadUrl(playlist.songs[1].imageUrl)
                image3.loadUrl(playlist.songs[2].imageUrl)
                image4.loadUrl(playlist.songs[3].imageUrl)
            } else if (playlist.songs.isNotEmpty()){
                uniqueImage.loadUrl(playlist.songs[0].imageUrl)
            }

            playlistName.text = playlist.playlistEntity.playlistName
            songCount.text = "${playlist.songs.size} Songs"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PlaylistWithSongs>() {
            override fun areItemsTheSame(
                oldItem: PlaylistWithSongs,
                newItem: PlaylistWithSongs,
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(
                oldItem: PlaylistWithSongs,
                newItem: PlaylistWithSongs,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}