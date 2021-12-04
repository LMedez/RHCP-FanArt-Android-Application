package com.luc.mymusic.playlistfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.luc.core.BaseFragment
import com.luc.mymusic.databinding.FragmentPlayListBinding
import com.luc.presentation.viewmodel.UserMusicDataViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlayListFragment : BaseFragment<FragmentPlayListBinding>(FragmentPlayListBinding::inflate) {

    private val userMusicDataViewModel: UserMusicDataViewModel by sharedViewModel()
    private val playListAdapter = PlayListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userMusicDataViewModel.clearPlaylist()

        binding.playlistFolderRecycler.adapter = playListAdapter

        userMusicDataViewModel.getPlayList().observe(viewLifecycleOwner) { playlist ->
            playListAdapter.submitList(playlist)
            binding.circularProgress.hide()
        }

//        binding.createPlaylist.setOnClickListener {
//            findNavController().navigate(PlayListFragmentDirections.actionPlayListFragmentToCreatePlayListFragment())
//        }
    }

}