package com.luc.albumdetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.luc.albumdetail.databinding.FragmentAlbumDetailBinding
import com.luc.common.capitalizeFirstChar
import com.luc.common.capitalizeWords
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.core.BaseFragment
import com.luc.presentation.viewmodel.AlbumDetailViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import com.luc.resources.adapter.SongItemFormat2Adapter
import com.luc.resources.animation.loadUrl
import com.luc.resources.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumDetailFragment :
    BaseFragment<FragmentAlbumDetailBinding>(FragmentAlbumDetailBinding::inflate) {
    private val args: AlbumDetailFragmentArgs by navArgs()

    private val albumDetailViewModel: AlbumDetailViewModel by viewModel()
    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()
    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()

    private val songItemFormat2Adapter = SongItemFormat2Adapter()

    override fun onResume() {
        super.onResume()
        musicPlayerViewModel.subscribeToMediaItems(args.album.title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition =
            MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true).addTarget(binding.root)
        returnTransition =
            MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ false).addTarget(binding.root)

        with(binding) {
            arrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
            songsRecycler.itemAnimator = null
            songsRecycler.adapter = songItemFormat2Adapter
            albumTitle.text = args.album.title.capitalizeFirstChar()
            producer.text =
                String.format(getString(R.string.album_producer) + " " +
                        args.album.producer.capitalizeWords() +
                        ", released in " + args.album.released)

            playShuffleButton.setOnClickListener {
                musicPlayerViewModel.shuffleModeEnabled.value?.let {
                    if (it) {
                        Snackbar.make(requireView(),
                            R.string.shuffleIsEnabled,
                            Snackbar.LENGTH_SHORT)
                            .setAction(R.string.disable) {
                                musicPlayerViewModel.disableShuffleMode()
                            }.show()
                    } else {
                        musicPlayerViewModel.enableShuffleMode()
                        Snackbar.make(requireView(), R.string.shuffleEnabled, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.undo) {
                                musicPlayerViewModel.disableShuffleMode()
                            }.show()
                    }
                }
            }
        }

        setUpImage()

        albumDetailViewModel.getSongsByAlbum(args.album.title).observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Success -> {
                    val songCount = it.data.size.toString() + " " + getString(R.string.songs)
                    binding.songCount.text = songCount
                    showSongs(it.data)
                }
                else -> {
                }
            }
        }

        userMusicDataViewModel.favSongs.observe(viewLifecycleOwner) {
            songItemFormat2Adapter.favSongList = it
        }

        songItemFormat2Adapter.setItemClickListener { songMetadata ->
            musicPlayerViewModel.playOrToggleSong(songMetadata, true)
        }

        songItemFormat2Adapter.setFavListener {
            userMusicDataViewModel.addOrRemoveFavSong(it)
        }

        musicPlayerViewModel.mediaSongPlaying.observe(viewLifecycleOwner) {
            songItemFormat2Adapter.curSong = it
        }

    }

    private fun setUpImage() {
        with(binding) {
            albumImage.loadUrl(args.album.imageUrl)
            albumImageBackground.loadUrl(args.album.imageUrl)
        }
    }

    private fun showSongs(list: List<SongMetadata>) {
        songItemFormat2Adapter.songs = list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicPlayerViewModel.unSubscribeToMediaItems()
    }
}
