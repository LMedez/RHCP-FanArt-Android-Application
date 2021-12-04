package com.luc.resources.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.luc.common.model.SongMetadata

abstract class BaseSongAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<BaseSongAdapter.SongViewHolder>() {

    class SongViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val binding = viewBinding
    }

    protected val diffCallback = object : DiffUtil.ItemCallback<SongMetadata>() {
        override fun areItemsTheSame(oldItem: SongMetadata, newItem: SongMetadata): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SongMetadata, newItem: SongMetadata): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<SongMetadata>

    var songs: List<SongMetadata>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding: ViewBinding =
            DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)

        return SongViewHolder(viewBinding)
    }

    protected var onItemClickListener: ((SongMetadata) -> Unit)? = null

    fun setItemClickListener(listener: (SongMetadata) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}

