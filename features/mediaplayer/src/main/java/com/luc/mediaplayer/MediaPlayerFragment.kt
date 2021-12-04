package com.luc.mediaplayer

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.luc.core.BaseFragment
import com.luc.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel.*
import com.luc.resources.animation.hide
import com.luc.resources.animation.show
import com.luc.resources.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {

    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicPlayerViewModel.playbackStates.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    PlayBackStates.STATE_BUFFERING -> {
                        playPauseButton.setImageResource(R.drawable.ic_play)
                    }
                    PlayBackStates.STATE_PLAYING -> playPauseButton.setImageResource(R.drawable.ic_pause)
                    PlayBackStates.STATE_PAUSED -> playPauseButton.setImageResource(R.drawable.ic_play)
                    else -> return@with
                }
            }
        }

        musicPlayerViewModel.shuffleModeEnabled.observe(viewLifecycleOwner) {
            if (it) {
                binding.shuffleActivated.show()
            } else binding.shuffleActivated.hide()
        }

        binding.shuffleActivated.setOnClickListener {
            musicPlayerViewModel.disableShuffleMode()
        }

        binding.queueButton.isEnabled = false

        binding.shuffle.setOnClickListener {
            musicPlayerViewModel.enableShuffleMode()
        }

        binding.skipNext.setOnClickListener {
            musicPlayerViewModel.skipToNextSong()
        }

        binding.skipToPrevious.setOnClickListener {
            musicPlayerViewModel.skipToPreviousSong()
        }

        binding.root.doOnPreDraw {
            findNavController().addOnDestinationChangedListener(onDestinationChange)
        }

        musicPlayerViewModel.showMediaPlayer.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id != R.id.albumDetailFragment)
                showFragment()
        }

        binding.playPauseButton.setOnClickListener {
            musicPlayerViewModel.playPauseMediaPlayer()
        }
    }

    private fun showFragment() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.clear(binding.contentContainer.id, ConstraintSet.TOP)
        constraintSet.connect(
            binding.contentContainer.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        val transition: Transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 500
        TransitionManager.beginDelayedTransition(binding.root, transition)
        constraintSet.applyTo(binding.root)
    }

    private val onDestinationChange =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.homeFragment && musicPlayerViewModel.mediaSongPlaying.value != null) {
                showFragment()
                Log.d("tests", parentFragment.toString())
            }
        }
}