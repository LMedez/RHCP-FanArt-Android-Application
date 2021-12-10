package com.luc.mymusic.playlistfragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.luc.core.BaseFragment
import com.luc.mymusic.R
import com.luc.mymusic.databinding.FragmentPlayListSheetBinding
import com.luc.presentation.viewmodel.PlaylistViewModel
import com.luc.resources.adapter.SongItemFormat4Adapter
import com.luc.resources.animation.*
import com.luc.resources.getColorFromAttr
import com.luc.resources.utils.doOnApplyWindowInsets
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlayListSheetFragment :
    BaseFragment<FragmentPlayListSheetBinding>(FragmentPlayListSheetBinding::inflate) {

    private val playlistViewModel: PlaylistViewModel by sharedViewModel()

    private val songItemFormat4Adapter = SongItemFormat4Adapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var normalTranslationX = 0f
        var translationXWithAlbumImage = 0f
        var shouldBeAnimate = true

        val behavior = BottomSheetBehavior.from(binding.playlistSheet)
        with(binding) {
            songsRecycler.adapter = songItemFormat4Adapter


            val backCallback =
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

            val sheetStartColor = playlistSheet.context.getColorFromAttr(R.attr.colorSecondary)
            val sheetEndColor = playlistSheet.context.getColorFromAttr(R.attr.colorSurface)
            val sheetBackground = MaterialShapeDrawable(
                ShapeAppearanceModel.builder(
                    playlistSheet.context,
                    R.style.ShapeAppearance_Rfa_MinimizedSheet,
                    0
                ).build()
            ).apply {
                fillColor = ColorStateList.valueOf(sheetStartColor)
                elevation = resources.getDimension(R.dimen.elevation_regular)
                initializeElevationOverlay(requireContext())
            }


            toolbar.setNavigationOnClickListener {
                backCallback.handleOnBackPressed()
            }
            behavior.isHideable = true
            clearButton.setOnClickListener {
                shouldBeAnimate = false
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            playlistSheet.background = sheetBackground

            playlistSheet.doOnLayout {

                val peek = behavior.peekHeight
                normalTranslationX = ((playlistSheet.width - peek) - 35).toFloat()
                translationXWithAlbumImage = normalTranslationX - albumImageContainer.width
                playlistSheet.translationX = playlistSheet.width.toFloat()

                behavior.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            playlistSheet.translationX = playlistSheet.width.toFloat()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            sheetBackground.interpolation = 1f
                        }
                        playlistViewModel.cancelTimerAnimation()
                        backCallback.isEnabled = newState == BottomSheetBehavior.STATE_EXPANDED
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (shouldBeAnimate) {
                            playlistSheet.translationX =
                                lerp(normalTranslationX, 0f, 0f, 0.15f, slideOffset)
                        sheetBackground.interpolation = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                        }
                        sheetBackground.fillColor = ColorStateList.valueOf(
                            lerpArgb(
                                sheetStartColor,
                                sheetEndColor,
                                0f,
                                0.3f,
                                slideOffset
                            )
                        )
                        playlistIcon.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                        albumImageContainer.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                        albumImageContainer.visibility =
                            if (slideOffset == 0f) View.VISIBLE else View.INVISIBLE
                        songCount.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                        sheetExpand.alpha = lerp(1f, 0f, 0f, 0.15f, slideOffset)
                        sheetExpand.visibility =
                            if (slideOffset < 0.5) View.VISIBLE else View.GONE
                        toolbar.alpha = lerp(0f, 1f, 0.2f, 0.8f, slideOffset)
                        songsRecycler.alpha = lerp(0f, 1f, 0.2f, 0.8f, slideOffset)
                    }
                })

                playlistSheet.doOnApplyWindowInsets { _, insets, _, _ ->
                    behavior.peekHeight =
                        peek + insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                }
            }

            sheetExpand.setOnClickListener {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }


        playlistViewModel.playlistSongAdded.observe(viewLifecycleOwner) {
            binding.songCount.text = it.toString()
            binding.toolbar.title = it.toString() + " " + getString(R.string.selected)
            songItemFormat4Adapter.songs = playlistViewModel.currentPlaylistSongs
        }

        playlistViewModel.showAlbumImage.observe(viewLifecycleOwner) {
            shouldBeAnimate = true

            it?.let {
                binding.albumImage.loadUrlWithAnimation(it)
                translateSheet(binding.playlistSheet, translationXWithAlbumImage, 100)


            } ?: run {
                translateSheet(binding.playlistSheet, normalTranslationX)
            }
        }

        playlistViewModel.clearPlaylist.observe(viewLifecycleOwner) {
            translateSheet(binding.playlistSheet, binding.playlistSheet.width.toFloat(), 100)
        }
    }

    private fun translateSheet(view: View, translationValue: Float, durationMs: Long = 200L) {
        view.animate().translationX(translationValue).apply { duration = durationMs }
    }
}