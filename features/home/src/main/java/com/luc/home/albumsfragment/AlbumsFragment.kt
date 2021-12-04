package com.luc.home.albumsfragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.luc.common.model.AlbumMetadata
import com.luc.core.BaseFragment
import com.luc.home.HomeFragmentDirections
import com.luc.home.databinding.FragmentAlbumsBinding
import com.luc.presentation.viewmodel.MusicDataViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import com.luc.resources.adapter.AlbumItemFormat2Adapter
import com.luc.resources.animation.show
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AlbumsFragment : BaseFragment<FragmentAlbumsBinding>(FragmentAlbumsBinding::inflate) {

    private val musicDataViewModel: MusicDataViewModel by sharedViewModel()
    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()
    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()
    private val albumItemFormat2Adapter = AlbumItemFormat2Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.albumsRecycler.adapter = albumItemFormat2Adapter

        musicDataViewModel.getAlbums().observe(viewLifecycleOwner) {
            showAlbums(it)
            binding.albumsCount.text = "${it.size} Albums"
            binding.contentContainer.show()
        }

        musicDataViewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) binding.circularProgress.show()
            else binding.circularProgress.hide()
        }

        albumItemFormat2Adapter.setFavListener { albumMetadata ->
            userMusicDataViewModel.addOrRemoveFavAlbum(albumMetadata)
        }

        albumItemFormat2Adapter.setAlbumPlayClickListener { albumMetadata ->
            musicPlayerViewModel.playAlbum(albumMetadata.title)
        }

        userMusicDataViewModel.favAlbums.observe(viewLifecycleOwner) {
            albumItemFormat2Adapter.favAlbumList = it
        }

        albumItemFormat2Adapter.setItemClickListener { album ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(album)
            )
        }
    }

    private fun showAlbums(list: List<AlbumMetadata>) {
        albumItemFormat2Adapter.albumList = list
    }
}