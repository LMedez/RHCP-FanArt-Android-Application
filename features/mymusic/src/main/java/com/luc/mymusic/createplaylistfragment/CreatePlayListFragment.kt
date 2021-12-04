package com.luc.mymusic.createplaylistfragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.luc.core.BaseFragment
import com.luc.mymusic.R
import com.luc.mymusic.databinding.FragmentCreatePlayListBinding
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import com.luc.resources.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreatePlayListFragment :
    BaseFragment<FragmentCreatePlayListBinding>(FragmentCreatePlayListBinding::inflate) {

    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()
    private val songRecyclerViewAdapter = SongItemAddToPlaylistAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.songsRecycler.adapter = songRecyclerViewAdapter

        if (userMusicDataViewModel.preparePlayList().songs.isEmpty()) {
            userMusicDataViewModel.favSongs.observe(viewLifecycleOwner) { favSongs ->
                songRecyclerViewAdapter.submitList(favSongs)
            }
        } else {
            songRecyclerViewAdapter.isFromPrepare = true
            songRecyclerViewAdapter.submitList(userMusicDataViewModel.preparePlayList().songs)
        }

        with(binding) {
            playListSave.setOnClickListener {
                userMusicDataViewModel.preparePlayList().playlistName =
                    playlistTitle.editText?.text.toString()
                userMusicDataViewModel.preparePlayList().playlistDescription =
                    playlistDescription.editText?.text.toString()

                userMusicDataViewModel.createPlayList()
            }
        }

        userMusicDataViewModel.playlistTitle.observe(viewLifecycleOwner) {
            if (!it) {
                showSnackBar(getString(R.string.snack_title_text), binding.root)
            }
        }

        songRecyclerViewAdapter.setCheckListener { songMetadata, isChecked ->
            if (isChecked) {
                userMusicDataViewModel.preparePlayList().songs.add(songMetadata)
            } else {
                userMusicDataViewModel.preparePlayList().songs.remove(songMetadata)
            }
        }

        binding.searchMusicButton.setOnClickListener {
            findNavController().navigate(CreatePlayListFragmentDirections.actionCreatePlayListFragmentToAllSongs())
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}