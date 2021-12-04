package com.luc.home.mymusicfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.luc.common.dateDiffer
import com.luc.common.entities.asSongMetadata
import com.luc.common.model.SongMetadata
import com.luc.core.BaseFragment
import com.luc.home.HomeFragmentDirections
import com.luc.home.R
import com.luc.home.databinding.FragmentMyMusicBinding
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import com.luc.resources.adapter.SongItemFormat3Adapter
import com.luc.resources.databinding.SongItemFormat3Binding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant.now
import java.util.*

class MyMusicFragment : BaseFragment<FragmentMyMusicBinding>(FragmentMyMusicBinding::inflate) {
    private val songItemFormat3Adapter = SongItemFormat3Adapter()

    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.songsRecycler.adapter = songItemFormat3Adapter

        userMusicDataViewModel.getLastSongsPlayedEntity.observe(viewLifecycleOwner) { songsList ->
            val list = mutableListOf<SongMetadata>()
            val listOfDates = mutableListOf<String>()
            songsList.forEach {
                listOfDates.add(dateDiffer(it.joined))
                list.add(it.songMetadataEntity.asSongMetadata())
            }
            songItemFormat3Adapter.listOfDates = listOfDates
            songItemFormat3Adapter.songs = list
            binding.songsRecycler.smoothScrollToPosition(0)
        }

        binding.favArrow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavFragment())
        }

        binding.allSongsArrow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAllSongs())
        }

        binding.allSongs.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAllSongs())
        }

        binding.favorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavFragment())
        }
    }

}