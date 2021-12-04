package com.luc.mymusic.allsongsfragment

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.luc.common.Constants
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.core.BaseFragment
import com.luc.mymusic.R
import com.luc.mymusic.databinding.FragmentAllSongsBinding
import com.luc.presentation.viewmodel.MusicDataViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.resources.adapter.SongItemFormat5Adapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AllSongsFragment : BaseFragment<FragmentAllSongsBinding>(FragmentAllSongsBinding::inflate) {

    private var previousBackStack: Int? = null
    private val musicDataViewModel: MusicDataViewModel by sharedViewModel()
    private val songItemFormat5Adapter = SongItemFormat5Adapter()
    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()
    private var songList: List<SongMetadata> = listOf()

    override fun onResume() {
        super.onResume()
        musicPlayerViewModel.subscribeToMediaItems(Constants.ALL_MUSIC)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true).addTarget(binding.root)
        returnTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false).addTarget(binding.root)

        previousBackStack = findNavController().previousBackStackEntry?.destination?.id

        //findNavController().previousBackStackEntry?.savedStateHandle?.set("key", songsSelected)

        binding.searchView.setOnQueryTextListener(searchListener)

        binding.allSongsRecycler.adapter = songItemFormat5Adapter

        musicDataViewModel.getAllSongs().observe(viewLifecycleOwner) {
            songList = it
            binding.songsCount.text = it.size.toString() + " " + getString(R.string.songs)
            songItemFormat5Adapter.songs = it
        }

        binding.arrowBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        songItemFormat5Adapter.setOnPlayListener {
            musicPlayerViewModel.playOrToggleSong(it)
        }

        binding.orderButton.setOnClickListener {
            songItemFormat5Adapter.songs = songList.sortedBy { it.albumName }
            binding.allSongsRecycler.smoothScrollToPosition(0)
        }
    }

    private val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?) = true

        override fun onQueryTextChange(query: String?): Boolean {
            if (query != null) {
                musicDataViewModel.getSongsByQuery("%$query%").observe(viewLifecycleOwner) {
                    songItemFormat5Adapter.songs = it
                }
            }
            return true
        }
    }
}