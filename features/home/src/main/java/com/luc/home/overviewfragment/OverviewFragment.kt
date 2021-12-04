package com.luc.home.overviewfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.luc.common.Constants.EXPLORE_MUSIC
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.core.BaseFragment
import com.luc.home.HomeFragmentDirections
import com.luc.home.R
import com.luc.home.databinding.FragmentOverviewBinding
import com.luc.presentation.viewmodel.MusicDataViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.resources.adapter.SongItemFormat1Adapter
import com.luc.resources.animation.hide
import com.luc.resources.animation.show
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OverviewFragment : BaseFragment<FragmentOverviewBinding>(FragmentOverviewBinding::inflate) {

    private val musicDataViewModel: MusicDataViewModel by sharedViewModel()
    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()
    private val songItemFormat1Adapter = SongItemFormat1Adapter()

    override fun onResume() {
        super.onResume()
        musicPlayerViewModel.subscribeToMediaItems(EXPLORE_MUSIC)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songsRecycler.adapter = songItemFormat1Adapter

        musicDataViewModel.limitSongs.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                showSongs(it)
            }
        })
        musicDataViewModel.albums.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showAlbums(it)
            }
        }

        songItemFormat1Adapter.setItemClickListener {
            musicPlayerViewModel.playOrToggleSong(it)
        }

        songItemFormat1Adapter.setPlayListener {
            musicPlayerViewModel.playOrToggleSong(it)
        }

        binding.seeAllAlbumsButton.setOnClickListener {
            val viewPager = parentFragment?.requireView()?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }
    }

    private fun showAlbums(albumList: List<AlbumMetadata>) {
        binding.popularAlbumsRecycler.adapter =
            PopularAlbumsRecyclerViewAdapter(albumList) { album ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(album)
                )
            }
    }

    private fun showSongs(songList: List<SongMetadata>) {
        songItemFormat1Adapter.songs = songList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicPlayerViewModel.unSubscribeToMediaItems()
    }

}