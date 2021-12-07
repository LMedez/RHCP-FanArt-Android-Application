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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}