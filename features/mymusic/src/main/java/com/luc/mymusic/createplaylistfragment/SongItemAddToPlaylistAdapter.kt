package com.luc.mymusic.createplaylistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luc.common.capitalizeFirstChar
import com.luc.common.model.SongMetadata
import com.luc.mymusic.databinding.ItemSongAddToPlaylistBinding
import com.luc.resources.animation.hide
import com.luc.resources.animation.loadUrl

class SongItemAddToPlaylistAdapter(
) : ListAdapter<SongMetadata, SongItemAddToPlaylistAdapter.SongRecyclerViewHolder>(DiffCallback) {

    private var onItemClickListener: ((SongMetadata, Int) -> Unit)? = null
    private var onCheckListener: ((SongMetadata, Boolean) -> Unit)? = null

    var isFromPrepare = false

    fun setItemClickListener(listener: (SongMetadata, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setCheckListener(listener: (SongMetadata, Boolean) -> Unit) {
        onCheckListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SongItemAddToPlaylistAdapter.SongRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSongAddToPlaylistBinding.inflate(layoutInflater, parent, false)
        val holder = SongRecyclerViewHolder(binding)

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.bindingAdapterPosition
            onCheckListener?.let { click ->
                click(getItem(position), isChecked)
            }
        }

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

    inner class SongRecyclerViewHolder(private val binding: ItemSongAddToPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongMetadata) = with(binding) {
            if (isFromPrepare) {
                checkbox.hide()
            }
            songName.text = song.title.capitalizeFirstChar()
            albumName.text = song.albumName
            albumImage.loadUrl(song.imageUrl)
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SongMetadata>() {
            override fun areItemsTheSame(oldItem: SongMetadata, newItem: SongMetadata): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SongMetadata, newItem: SongMetadata): Boolean {
                return oldItem == newItem
            }
        }
    }
}