package com.luc.mymusic.favoritefragment

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.luc.albumdetail.AlbumDetailFragmentArgs
import com.luc.common.Constants
import com.luc.core.BaseFragment
import com.luc.mymusic.R
import com.luc.mymusic.databinding.FragmentFavoriteBinding
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import com.luc.resources.adapter.AlbumItemFormat3Adapter
import com.luc.resources.adapter.SongItemFormat1Adapter
import com.luc.resources.dimensionFromAttribute
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()
    private val songItemFormat1Adapter = SongItemFormat1Adapter()
    private val albumItemFormat3Adapter = AlbumItemFormat3Adapter()
    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()

    override fun onResume() {
        super.onResume()
        musicPlayerViewModel.subscribeToMediaItems(Constants.FAVORITE_MUSIC)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true).addTarget(binding.root)
        returnTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false).addTarget(binding.root)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true).addTarget(binding.root)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false).addTarget(binding.root)

        binding.favRecyclerView.adapter = songItemFormat1Adapter
        binding.albumsRecyclerView.adapter = albumItemFormat3Adapter
        binding.arrowBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        songItemFormat1Adapter.setPlayListener {
            musicPlayerViewModel.playOrToggleSong(it)
        }

        songItemFormat1Adapter.setItemClickListener {
            musicPlayerViewModel.playOrToggleSong(it)
        }

        userMusicDataViewModel.favSongs.observe(viewLifecycleOwner) {
            songItemFormat1Adapter.songs = it
        }

        userMusicDataViewModel.favAlbums.observe(viewLifecycleOwner) {
            albumItemFormat3Adapter.albumList = it
        }

        albumItemFormat3Adapter.setItemClickListener {
            findNavController().navigate(FavoriteFragmentDirections.actionFavFragmentToAlbumDetailFragment(it))
        }
    }


}