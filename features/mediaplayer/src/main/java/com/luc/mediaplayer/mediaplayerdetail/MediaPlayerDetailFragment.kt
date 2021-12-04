package com.luc.mediaplayer.mediaplayerdetail

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.slider.Slider
import com.luc.common.capitalizeFirstChar
import com.luc.core.BaseFragment
import com.luc.mediaplayer.databinding.FragmentMediaPlayerDetailBinding
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.resources.utils.height
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MediaPlayerDetailFragment :
    BaseFragment<FragmentMediaPlayerDetailBinding>(FragmentMediaPlayerDetailBinding::inflate) {

    private val musicPlayerViewModel: MusicPlayerViewModel by sharedViewModel()

    private var shouldUpdateSeekBar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekBar.addOnSliderTouchListener(onSeekBarChange)

        binding.seekBar.setLabelFormatter { value ->
            musicPlayerViewModel.timestampToMSS(value.toLong())
        }

        musicPlayerViewModel.playbackStates.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    MusicPlayerViewModel.PlayBackStates.STATE_BUFFERING -> circularProgress.show()
                    else -> circularProgress.hide()
                }
            }
        }

        musicPlayerViewModel.mediaSongPlaying.observe(viewLifecycleOwner) {
            binding.albumName.text = it.albumName.capitalizeFirstChar()
            Glide.with(this).load(it.imageUrl).into(binding.albumImage)
            binding.songName.text = it.title.capitalizeFirstChar()
        }

        musicPlayerViewModel.showMediaPlayer.observe(viewLifecycleOwner) {
            if (it) expandMediaPlayerDetail()
            else shrinkMediaPlayerDetail()
        }

        binding.showDetailButton.setOnClickListener {
            musicPlayerViewModel.switchMediaPlayerVisibility(true)
        }

        musicPlayerViewModel.mediaPosition.observe(viewLifecycleOwner) {
            with(binding) {
                if (shouldUpdateSeekBar) {
                    seekBar.value = it.toFloat()
                    songTimeStart.text = musicPlayerViewModel.timestampToMSS(it)
                }
            }
        }

        musicPlayerViewModel.mediaDuration.observe(viewLifecycleOwner) {
            if (it != -1L) {
                binding.seekBar.valueTo = it.toFloat()
            }
            binding.songTimeEnd.text = musicPlayerViewModel.timestampToMSS(it)
        }
    }

    private fun expandMediaPlayerDetail() {
        with(binding) {
            root.animate().translationY(0f).setDuration(200).interpolator =
                AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
            showDetailButton.animate().scaleY(0f).scaleX(0f)
        }
    }

    private fun shrinkMediaPlayerDetail() {
        with(binding) {
            container.height {
                root.animate().translationY(it.toFloat()).setDuration(200).withEndAction {
                    showDetailButton.animate().scaleX(1f).scaleY(1f)
                }.interpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
            }
        }
    }

    private val onSeekBarChange = object : Slider.OnSliderTouchListener {

        override fun onStartTrackingTouch(slider: Slider) {
            shouldUpdateSeekBar = false
        }

        override fun onStopTrackingTouch(slider: Slider) {
            slider.run {
                musicPlayerViewModel.seekTo(this.value.toLong())
                shouldUpdateSeekBar = true
            }
        }
    }
}

