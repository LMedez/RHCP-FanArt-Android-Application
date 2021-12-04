package com.luc.resources.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.luc.common.model.AlbumMetadata

abstract class BaseAlbumAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<BaseAlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val binding = viewBinding
    }

    protected val diffCallback = object : DiffUtil.ItemCallback<AlbumMetadata>() {
        override fun areItemsTheSame(oldItem: AlbumMetadata, newItem: AlbumMetadata): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: AlbumMetadata, newItem: AlbumMetadata): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<AlbumMetadata>

    var albumList: List<AlbumMetadata>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding: ViewBinding =
            DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)

        return AlbumViewHolder(viewBinding)
    }

    protected var onItemClickListener: ((AlbumMetadata) -> Unit)? = null

    fun setItemClickListener(listener: (AlbumMetadata) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return albumList.size
    }
}